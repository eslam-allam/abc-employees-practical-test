# ABC Bank SpringBoot Practical Assignment

## Notes

- The implementation details of all endpoints were based on the question
  description and given examples.
- No alternations were made to the implied data types from the examples (e.g.
  Salary was given as a string so it was implemented as a string)

## How to Use

- Go to [application.properties](./src/main/resources/application.properties)
  and change the **spring.data.fs.path** property to the full path where you
  want the employee data to reside.
- Run the app using `gradlew bootRun` or `sudo gradlew bootRun` if the data path
  requires elevated permissions.
- Sample requests can be found in the [rest](./rest) directory.
