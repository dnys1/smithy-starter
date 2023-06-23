$version: "2"

namespace com.amazonaws.amplify.test

use aws.protocols#restJson1

// Dummy service required for code generation.
//
// Services are the entrypoint for generation, so all types must fall under a service's "closure" to be eligible
// for generation. Thus, we define a dummy service which bootstraps generation of all the types.
@restJson1
service TestService {
    version: "0.1.0"
    operations: [RunTest]
}

operation RunTest {
    input: RunTestRequest
}

structure RunTestRequest {
    @required
    test: Test
}

// Write the test schema below here.

structure Test {}
