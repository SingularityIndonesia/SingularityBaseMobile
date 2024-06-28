![Illustration](images/Centralized%20Context%20Control.jpg)  
  
**Centralized context control** is a concept where the main context must be able to see and control all of its constituent subcontexts.  
  
The principles are as follows:  
  
1. **Single main context**: An application has one main context.  
2. **Multiple subcontexts**: A main context can have multiple subcontexts.  
3. **Context registry policy**: Main context as a context registry, registering contexts from its constituents.  
4. **Main context must aware of all subcontexts**: A main context must be able to see and directly-control all of its subcontexts.<br>For example, let say an application have authentication feature; The auth feature must not have an independent context without the knowledge of the main context. In other words, the `auth feature` it self cannot exist (in the project and runtime) if the main context doesn't permit the `auth context` tobe exist or main context doesn't know that `auth context` exists.  
5. **Only the main context can reify or create contexts**. Features can define their context requirements or their abstract context, but they cannot create context instances for themselves.  
  
Purpose:  
- To prevent submodules to access the Environment variable directly.  
- To make the main app aware of all the submodules.  
- To make the main app able to control all context and all instances in the module.  
  
Example:  
```kotlin  
// Context specific function.  
package example.data  

suspend fun Context.GetTodos(): Result<List<Todo>> =
	withContext(Dispatchers.Default) {
		webRepositoryContext.webClient.get("todos/")
			.map {                
				val stringResponse = it.invoke().decodeToString()
				Json.decodeFromString<List<Todo>>(stringResponse)
			}
	}  

//Context of Example Module  
package example.model  
data class Context(
	val webRepositoryContext: WebRepositoryContext
)  
  
// GetTodos need this web client instance to run  
package core.context  
interface WebRepositoryContext {
	val webClient: WebClient
}  
```  
That function requires Example Module's Context to be able to run. That function cannot exist nor tobe running if the main context doesn't permit the `Example Module's Context` tobe exist.  
  
To permit that function to run, the main context must reify or create the `ExampleContext` and it's `WebRepositoryContext` first.  
  
**Example of MainContext class in main module:**  
```kotlin  
package main  
  
// Main context, control and aware of all subcontexts  
class MainContext {  
	// ExampleModule's Context
	val exampleContext: ExampleContext by lazy {
		ExampleContext(
			webRepository = ExampleWebRepository(
				EnvironmentVariables.webHost,
				EnvironmentVariables.todoBasePath
			)
		)
	}  
    val aiChatContext: AIChatContext by lazy {
		AIChatContext(
			geminiApiKey = EnvironmentVariables.geminiApiKey
		)
	}  
    val dashboardContext: DashboardContext by lazy {
		DashboardContext(
			exampleContext = exampleContext,
			aiChatContext = aiChatContext
		)
	}
}  
  
// Main module create the ExampleModule's context and sub context.  
// Submodule cannot create its own sub contexts.  
package main.modules  
  
class ExampleWebRepositoryContext(
	private val host: String,
	private val basePath: String
) : WebRepositoryContext {  
	override val webClient: WebClient by lazy {
		defaultWebClient(host,basePath)
	}
}
```