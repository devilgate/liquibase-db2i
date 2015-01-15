# liquibase-db2i: Liquibase DB2 for iSeries support

Even without this extension, most Liquibase features work with DB2 for IBM'S midrange server -- the machine formerly known as AS/400, iSeries and System i, and now seemingly [officially known](http://en.wikipedia.org/wiki/IBM_Power_Systems) as the [IBM Power Systems](http://www-03.ibm.com/systems/uk/i/). 

There are a few subtleties, however, that make DB2 for i (let's call it that for shortish) different from DB2 LUW. This extension handles those.

# Installing the extension

The core Liquibase documentation is curiously weak on how to install and use extensions. Luckily there is a good description on [how to get, build and use this very extension in this blog post](http://blog.awolski.com/installing-liquibase-extensions/) by "awolkski".

# Particluars

In particular it copes with:

- the fact that DB2 for i does not use the `TABLESPACE` SQL clause
- the fact that some lengths have limits (more of which below)

## Length limits

 DB2 for i has limits on the lengths of table and column comments. Previously if a Liquibase changeset included a `remarks=` attribute with a value longer than the limit, the generated SQL would include the full text, and cause an error when it was executed.

 Now the value will be truncated at three characters short of the maximum length, and an ellipsis (...) placed in the last three characters to indicate the truncation.

 So for example, a changeset like this:

`````XML
<createTable
        remarks="This is the main table where ExampleApplication keeps its details about all the things."
        tableName="THINGS"
...
</createTable>        
`````

will now generate SQL like this:

`````SQL
LABEL ON TABLE SCHEMA.THINGS IS 'This is the main table where ExampleApplication...';
`````
Which is not ideal, but at least it will run.
