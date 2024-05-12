# ApiGenerator
ApiGenerator is a Gradle plugin that I created to automatically generate API clients and response models. You simply need to apply the plugin to your Gradle build as follows:
``` kotlin
plugins {
    ...
    // you will need kotlin serialization as well
    kotlin("plugin.serialization")
    // api generator
    id("ApiGenerator")
}

kotlin {
    sourceSets {
        ...
        commonMain.dependencies {
            ...
            // make sure you add ktor to the dependencies
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.cio)
            implementation(libs.kotlinx.serialization.json)
        }
        iosMain.dependencies {
          implementation(libs.ktor.client.ios)
        }
    }
}

```

Next, you only need to create the API contracts and place them anywhere you want within the module.
Contract Example:
``` json
{
  "context": "Cart",
  "endpoint": "cart.php",
  "methods": [
    {
      "method": "PUT",

      // header
      "header": {
        "barer": {
          "type": "string",
          "optional": false
        }
      },

      // request 
      "request": {
        "productID": {
          "type": "string",
          "optional": true
        },
        "amount": {
          "type": "int",
          "optional": false
        }
      },

      // response
      "response": {
        "type": "object",
        "schema" : {
          "success": {
            "type": "boolean"
          },
          "test_list": {
            "type": "list",
            "name": "TestListItem",
            "schema": {
              "id": {
                "type": "string"
              }
            }
          },
          "data": {
            "type": "object",
            "schema": {
              "items": {
                "type": "list",
                "name": "CartItem", // important! for type string you need to define name
                "schema": {
                  "productID": {
                    "type": "string"
                  },
                  "amount": {
                    "type": "int"
                  }
                }
              }
            }
          }
        }
      }
    }
  ]
}
```

# Important
The API contract files must be named with the suffix `.apicontract.json`. ex: `cart.apicontract.json`.

Your code will be generated in `build/generated/kotlin/apigenerator` directory.
