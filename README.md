[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.lamblin/lamblin-core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/cz.jirutka.rsql/rsql-parser)
[![Chat at https://gitter.im/org-lamblin/general](https://badges.gitter.im/org-lamblin/general.svg)](https://gitter.im/org-lamblin/general)
[![CircleCI](https://circleci.com/gh/BorislavShekerov/lamblin.svg?style=shield)](https://circleci.com/gh/BorislavShekerov/lamblin)
[![codebeat badge](https://codebeat.co/badges/23c4a019-76f3-4999-af29-66a9fd188d19)](https://codebeat.co/projects/github-com-borislavshekerov-lamblin-master)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Awesome Kotlin Badge](https://kotlin.link/awesome-kotlin.svg)](https://github.com/KotlinBy/awesome-kotlin)

# lamblin
A REST Framework for Kotlin or Java applications deployed as AWS Lambdas.

## Examples
This examples to follow are in written in Kotlin only, however you can find their Java versions and more at [docs](http://lamblin.org).

## Quickstart

### Add dependency

```xml
<dependency>
    <groupId>org.lamblin</groupId>
    <artifactId>lamblin-core</artifactId>
    <version>0.1.6</version>
</dependency>

// or gradle, if you must
compile 'org.lamblin:lamblin-core:0.1.6'
```


### Define your controller
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

### Delegate the routing logic to Kotlin
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

Lamblin provides a set of standard annotations which you can use in your endpoints to drive routing, inject specific request arguments, implement access control etc.


