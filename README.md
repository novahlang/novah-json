# novah-json

A library for writing and reading Novah types to/from JSON.

## Usage

Add `novah-json` to your dependencies.

TODO

## Writing JSON

You can use `writeDefault` to write values that novah-json knows how to convert to json
like lists, records, primitives, etc.

Example:
```novah
import novah.json as JSON

typealias Address =
  { street : String
  , house : String
  , zipCode : Option Int
  }

someAddress : Address
someAddress =
  { street: "street name"
  , house: "13A"
  , zipCode: Some 12345
  }

addressJson = JSON.writeDefault someAddress
// => {"zipCode":12345,"house":"13A","street":"street name"}
```

### Custom types

If you want you own defined types to work with `writeDefault` you need to provide a serializer.

Example:
```novah
foreign import com.fasterxml.jackson.core.JsonGenerator

import novah.json as JSON

type Color
  = Red
  | Green
  | Blue

writeColor : JsonGenerator -> Color -> Unit
writeColor (gen : JsonGenerator) =
  case _ of
    Blue -> gen#writeString("blue")
    Red -> gen#writeString("red")
    Green -> gen#writeString("green")

pub
main : Array String -> Unit
main _ =
  // add the serializer for this class globally
  JSON.addSerializer Color#-class writeColor
  JSON.writeDefault Blue
```

## Pretty printing

TODO

## Reading JSON

Reading needs custom made parsers for records as Novah cannot check them automatically.

Example:

```novah
readAddress : JsonNode -> Result Address String
readAddress node =
  do.result
    let! obj = JSON.asObject node
    let! street = JSON.readStringFieldReq obj "street"
    let! house = JSON.readStringFieldReq obj "house"
    let! zipCode = JSON.readInt32Field obj "zipCode"
    return { street
           , house
           , zipCode
           }

addressJson : String
addressJson =
  """
  {
    "street": "street name",
    "house": "13A",
    "zipCode": 12345
  }
  """

parsedAddress : Result Address String
parsedAddress = JSON.readWith readAddress addressJson
```