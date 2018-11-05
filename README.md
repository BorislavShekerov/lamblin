[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.lamblin/lamblin-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/cz.jirutka.rsql/rsql-parser)
[![Chat at https://gitter.im/org-lamblin/general](https://badges.gitter.im/org-lamblin/general.svg)](https://gitter.im/org-lamblin/general)
[![CircleCI](https://circleci.com/gh/BorislavShekerov/lamblin.svg?style=shield)](https://circleci.com/gh/BorislavShekerov/lamblin)
[![codebeat badge](https://codebeat.co/badges/23c4a019-76f3-4999-af29-66a9fd188d19)](https://codebeat.co/projects/github-com-borislavshekerov-lamblin-master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)

:star: __Lamblin__ :star: is a simple and lightweight web-framework for __AWS Lambda__ functions written in __Kotlin__ or __Java__.
  Unlike the other traditional _JVM_ web-frameworks (e.g <a href="https://spring.io/projects/spring-boot">Spring</a>, <a href="https://www.playframework.com">Play</a>, <a href="http://sparkjava.com">Spark</a>) which targeted an execution model where our application was deployed to an HTTP application container (e.g _Apache_, _Jetty_), Lamblin was developed to target the AWS Lambda execution environment specifically.
  
  As such it:
  * Introduces __no runtime overhead__
  * __Integrates seemlessly__ with API Gateway proxied requests
  * Makes it __easy to run your Lambda locally__ by hosting it in an API Gateway-like HTTP container
  * Offers custom test runners which make it __easy to integration test__ your Lambda endpoints with HTTP requests

## Examples
This examples to follow are in written in Kotlin only, however you can find the corresponding Java versions and more at the [docs site](http://lamblin.org).

## Quickstart

##### Add dependency

```xml
<dependency>
    <groupId>org.lamblin</groupId>
    <artifactId>lamblin-core</artifactId>
    <version>0.1.6</version>
</dependency>

// or gradle, if you must
compile 'org.lamblin:lamblin-core:0.1.6'
```

##### API Gateway setup

Lamblin handles the routing login withing the executing _Lambda which means that the _API Gateway_ definition remains fairly minimalistic. Instead of configuring each of the resources(and their subresources) that trigger a specific _Lambda_(via proxy integration), <span class="code">Lamblin</span> relies on the use of <a href="https://aws.amazon.com/blogs/aws/api-gateway-update-new-features-simplify-api-development/" target="blank">catch-all path variables.</a></b> <br>
The example API Gateway configuration below will capture all requests to /todo-lists/** and proxy them to our _Lambda_.

![Example API Gateway setup](https://s3-eu-west-1.amazonaws.com/www.lamblin.org/assets/api-gateway.png)

##### Define your controller
```kotlin
@Controller
class TodoListController(private val dataService: DataService) {

    @Endpoint("/todo-lists", HttpMethod.GET)
    fun todoLists() = HttpResponse.ok(dataService.fetchAll())

    @Endpoint("/todo-lists/{id}", HttpMethod.GET)
    fun getTodoList(@PathParam("id") listId: String) =
        HttpResponse.ok(dataService.fetchList(listId))
}
```

##### Delegate the routing logic to Kotlin
```kotlin
// Developers are responsible for injecting their controller dependencies
val dataService = DataService.inMemory()
val lamblin = Lamblin.frontController(TodoListController(dataService))

class LambdaHandler : RequestStreamHandler {

  override fun handleRequest(input: InputStream, output: OutputStream, p2: Context) {
      lamblin.handlerRequest(input, output)
  }
}
```

## Anotations

Lamblin provides a set of standard annotations which you can use in your endpoints to drive routing, inject specific request arguments, implement access control and others.


###### @Controller

A marker annotation defining that a specific class hosts a set of endpoints.
```kotlin
@Controller
class TodoController {
}
```

###### @Endpoint

Each endpoint handler method should be marked as `@Endpoint`. This annotation defines the request path and
HTTP method that the handler is responsible for.

The example below defines an endpoint which handles `GET` requests to `/todo-lists`.
Lamblin handles injection of specific request attributes (e.g query and path parameteres, request body) using the
annotations described in the following sections. 

Every endpoint handler method should return a  `HttpResponse` instance, defining the response status code, response body and any additional response headers to be forwarded to the client.

```kotlin
@Controller
class TodoListController {

  ...
   
  @Endpoint("/todo-lists", HttpMethod.GET)
  fun fetchTodoLists(): HttpResponse<List<TodoList>> {
      return HttpResponse.ok(todoListDataService.fetchTodoLists())
  }

}
```

###### @QueryParam

`@QueryParam` allows injection of query parameter in your endpoint handler methods.
`@QueryParam` parameters are required by default, meaning that if an `@Endpoint` method expects a given
 `@QueryParam` and the request received does not contain the query param expected, the handler will not be triggered.

 In the example below, if a `GET /todo-lists` request is received without the expected _name_ query param
`fetchTodoListForName` will not be triggered and Lamblin will return a _404_.

```kotlin
@Controller
class TodoListController {

  ...

  @Endpoint("/todo-lists", HttpMethod.GET)
  fun fetchTodoListForName(@QueryParam("name") name: String): HttpResponse<TodoList> {
      return HttpResponse.ok(todoListDataService.fetchTodoListForName(name))
  }

}
```

In order for a query param to be marked as optional, the _required_ parameter of `@QueryParam` can be used e.g `@QueryParam("query", required = false)`.
In the example below, if a `GET /todo-lists` request is received without the optional _name_ query param `fetchTodoListForName` will still be triggered.

```kotlin
@Controller
class TodoListController {

  ...

  @Endpoint("/todo-lists", HttpMethod.GET)
  fun fetchTodoListForName(
    @QueryParam("name") name: String, 
    @QueryParam("status", required = false) status: String?): HttpResponse<TodoList> {

      return HttpResponse.ok(todoListDataService.fetchTodoListForName(name, status))
  }

}
```

In case you want to provide a default value for a missing query param, the `defaultValue` parameter of `@QueryParam` can be used.
In the example below, if a `GET /todo-lists` request is received without the optional _name_ query param
`fetchTodoListForName` will still be triggered and the default value _10_ will be injected in the limit parameter.

```kotlin
@Controller
class TodoListController {

  ...

  @Endpoint("/todo-lists", HttpMethod.GET)
  fun fetchTodoListForName(
    @QueryParam("name") name: String,
    @QueryParam("limit", defaultValue = "10") limit: Int): HttpResponse<TodoList> {

      return HttpResponse.ok(todoListDataService.fetchTodoListForName(name,limit))
  }

}
```

###### @PathParam

`@PathParam` allows injection of path parameters in your endpoint handler methods. In the example below, if a
`GET /todo-lists/bar` request is received, the value _bar_ will be injected in the `uid` parameter.

```kotlin
@Controller
class TodoListController {

  ...

  @Endpoint("/todo-lists/{uid}", HttpMethod.GET)
  fun fetchTodoList(@PathParam("uid") uid: String): HttpResponse<TodoList> {
      return HttpResponse.ok(todoListDataService.fetchTodoList(uid))
  }

}
```

###### @Header

`@Header` allows injection of _HTTP request headers_ in your endpoint handler methods. In the example below, if a
`userUid` header is present its value will be injected in the _userUid_ parameter. It is
__important__ to consider whether the header parameter should be of nullable type or not in order to prevent any
`RuntimeException` being thrown when the header is missing.

```kotlin
@Controller
class TodoListController {

  ...

  @Endpoint("/todo-lists", HttpMethod.GET)
  fun fetchUserTodoLists(@Header("userUid") userUid: String?): HttpResponse<TodoList> {
      return HttpResponse.ok(todoListDataService.fetchTodoListsForUser(userUid))
  }

}
```

###### @RequestBody

`@RequestBody` can be used in order to deserialize the contents of the _HTTP request body_ (provided that the request
 method supports body payload).

```kotlin

@Controller
class TodoListController {

  ...

  @Endpoint("/todo-lists", HttpMethod.POST)
  fun fetchTodoListForName(@RequestBody creationRequest: TodoListCreationRequest): HttpResponse<TodoList> {
      return HttpResponse.ok(todoListDataService.createTodoList(creationRequest))
  }

}

```

###### @AccessControl

`@AccessControl` can be used if you want to specify an Authorizer for a specific endpoint. The authorizer should implement `RequestAuthorizer`.
The snippet below shows an example `@AccessControl` use.

```kotlin

@Controller
class TodoListController {

  ...

  @AccessControl(["USER"], RoleBasedAuthorizer::class)
  @Endpoint("/todo-lists", HttpMethod.GET)
  fun fetchTodoLists(): HttpResponse<List<TodoList>> {
      return HttpResponse.ok(todoListDataService.fetchTodoLists())
  }
}

object RoleBasedAuthorizer: RequestAuthorizer {

  override fun isRequestAuthorized(acceptedRoles: Array<String>, request: APIGatewayProxyRequestEvent): Boolean {
    // Implement your authorization logic here
    // e.g. Decode JWT from Authorization header and validate its roles against the accepted roles
  }
}

```

###### APIGatewayProxyRequestEvent

 In case you want to work with the raw `APIGatewayProxyRequestEvent<` proxied from API Gateway, you can declare a handler parameter of type `APIGatewayProxyRequestEvent` and Lamblin will make sure to inject that.
This should be used as a fallback mechanism if Lamblin does not provide injection of the request section that you are interested in.

```kotlin
@Controller
class TodoListController {

  ...

  @Endpoint("/todo-lists", HttpMethod.GET)
  fun fetchTodoListForName(requestEvent: APIGatewayProxyRequestEvent): HttpResponse<TodoList> {
      val listName = requestEvent.queryStringParameters["name"]

      return HttpResponse.ok(todoListDataService.getTodoListForName(listName))
  }
```

## Local Runner

Taking the <a href="https://stackify.com/function-as-a-service-serverless-architecture/" target="blank">FaaS</a> approach means we don't need to manage any infrastructure which sounds great. Unfortunately it does introduce some pain points one of which is running the application locally.
In the old deployment model, we would deploy our artifact to an HTTP Container (i.e. Tomcat, Jetty, etc.) or include the container in our deployable, so running the application locally would be just a matter of deploying our app to a local server or executing the deployable, respectively.

Lamblin's `LocalRunner` allows us to run our Lambda endpoints locally in a similar manner by wrapping it with a Jetty HTTP Container(using <a href="https://javalin.io/" target="blank">Javalin</a>) as a substitute for <span class="code">API Gateway</span>.

Installation(mvn example):
```xml
<dependency>
  <groupId>org.lamblin</groupId>
  <artifactId>lamblin-local-runner</artifactId>
  <version>0.1.6</version>
</dependency>
```

:warning: Lamblin's `LocalRunner` is not intended for production use, it should only be used to test or debug your Lamblin handlers locally.

## Test Runners

We are all well-aware that we should do most of our testing at the lowest(unit) level and gradually reduce the amount of testing as we go up the <a href="https://d2h1nbmw1jjnl.cloudfront.net/ckeditor/pictures/data/000/000/158/content/typical_pyramid-1024x938.jpg" target="blank">pyramid</a>.
Unit testing you domain logic code (ideally packaged separately as an API/library) is no different when using <span class="code">Lambda</span>, the complexity increases when we want to write some automated integration tests i.e. running our Lambda endpoints' handler code and executing some HTTP requests against it.
_AWS_ does not provide a Lambda integration test library, __but worry not__ Lamblin does!
Lamblin provides custom `JUnit4` and `JUnit5` runners which host your Controllers in a <span class="code">HTTP</span> container allowing you to execute requests against them.

Installation:
```xml
<dependency>
  <groupId>org.lamblin</groupId>
  <artifactId>lamblin-test</artifactId>
  <version>0.1.6</version>
</dependency>
```


JUnit4LamblinTestRunner test runner example:

```kotlin
@RunWith(JUnit4LamblinTestRunner.class)
@LamblinTestRunnerConfig(testConfigClass = FooTest.FooTestConfiguration.class)
class FooTest {

  ...

  class FooTestConfiguration : LamblinTestConfig {

    override fun controllers() = setOf(FooController())
  }
}
```

JUnit5LamblinExtension test runner example:

```kotlin
@ExtendWith(JUnit5LamblinExtension::class)
@LamblinTestRunnerConfig(testConfigClass = FooTest.FooTestConfiguration.class)
class FooTest {

  ...

  @Test
  fun \`should respond with 200\`() {
    // Send a HTTP request to your controller and assert on the response
  }

  class FooTestConfiguration : LamblinTestConfig {
    override fun controllers() = setOf(FooController())
  }
}
```
## Plugins

##### Slack alerting plugin

Implementation in progress...

