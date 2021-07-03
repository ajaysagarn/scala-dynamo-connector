package com.chore.crunch
package connection

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.alpakka.dynamodb.scaladsl.DynamoDb
import akka.stream.scaladsl.{Sink, Source}
import software.amazon.awssdk.auth.credentials.{AwsBasicCredentials, StaticCredentialsProvider}
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.core.internal.retry.SdkDefaultRetrySetting
import software.amazon.awssdk.core.retry.RetryPolicy
import software.amazon.awssdk.core.retry.backoff.BackoffStrategy
import software.amazon.awssdk.core.retry.conditions.RetryCondition
import software.amazon.awssdk.http.async.SdkAsyncHttpClient
import software.amazon.awssdk.http.nio.netty.NettyNioAsyncHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbAsyncClient
import software.amazon.awssdk.services.dynamodb.model.{AttributeDefinition, AttributeValue, BatchGetItemRequest, BatchGetItemResponse, CreateTableRequest, CreateTableResponse, DescribeTableRequest, DescribeTableResponse, KeysAndAttributes, ListTablesRequest, ListTablesResponse, PutItemRequest, PutItemResponse, ScalarAttributeType}

import java.net.URI
import scala.::
import scala.concurrent.Future

object DynamoConnector extends App{

  implicit val system = ActorSystem("CHORE-CRUNCH")

  private val region = Region.US_EAST_1
  val customClient: SdkAsyncHttpClient = NettyNioAsyncHttpClient.builder().maxConcurrency(100).build()
  implicit val client: DynamoDbAsyncClient = DynamoDbAsyncClient
    .builder()
    .endpointOverride(URI.create("http://localhost:8000"))
    .region(region)
    .httpClient(customClient)
    .overrideConfiguration(
      ClientOverrideConfiguration
        .builder()
        .retryPolicy(
          // This example shows the AWS SDK 2 `RetryPolicy.defaultRetryPolicy()`
          // See https://sdk.amazonaws.com/java/api/latest/software/amazon/awssdk/core/retry/RetryPolicy.html
          RetryPolicy.builder
            .backoffStrategy(BackoffStrategy.defaultStrategy)
            .throttlingBackoffStrategy(BackoffStrategy.defaultThrottlingStrategy)
            .numRetries(SdkDefaultRetrySetting.DEFAULT_MAX_RETRIES)
            .retryCondition(RetryCondition.defaultRetryCondition)
            .build
        )
        .build()
    ).build();

  system.registerOnTermination(client.close())


  def createTable(request: CreateTableRequest) = {
    val source = Source.single(request)
      .via(DynamoDb.flow(parallelism = 1))
  }

  val attributes:List[AttributeDefinition] = List(
    AttributeDefinition.builder().attributeName("nodeId").attributeType(ScalarAttributeType.S).build(),
    AttributeDefinition.builder().attributeName("attributes").attributeType(ScalarAttributeType.S).build()
  )

  //attributes + AttributeDefinition.builder().attributeName("nodeId").attributeType(ScalarAttributeType.S).build()

  val request = CreateTableRequest.builder().tableName("Test_Table")
              .attributeDefinitions()


  val source: Source[DescribeTableResponse, NotUsed] = Source
    .single(CreateTableRequest.builder().tableName("testTable").build())
    .via(DynamoDb.flow(parallelism = 1))
    .map(response => {
      print("------------Heres the response---------------------")
      print(response)
      DescribeTableRequest.builder().tableName(response.tableDescription.tableName).build()
    })
    .via(DynamoDb.flow(parallelism = 1))

  source.runWith(Sink.last)

  val listTablesResult: Future[ListTablesResponse] =
    DynamoDb.single(ListTablesRequest.builder().build())

  print(listTablesResult)
}
