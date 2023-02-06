# BookPlus Service
The web service has been built with the Spring framework

# Index
<ul>
<li>Response structure</li>
<li>APIs
  <ul>
    <li>Book Management
      <ul>
        <li><a href="#list">List</a></li>
        <li><a href="#availability-check">Availability Check</a></li>
        <li><a href="#add">Add</a></li>
        <li><a href="#delete">Delete</a></li>
        <li><a href="#edit">Edit</a></li>
        <li><a href="#find">Find</a></li>
      </ul>
    </li>
    <li>Student Management
      <ul>
        <li><a href="#list-1">List</a></li>
        <li><a href="#add-1">Add</a></li>
        <li><a href="#delete-1">Delete</a></li>
        <li><a href="#edit-1">Edit</a></li>
        <li><a href="#find-1">Find</a></li>
      </ul>
    </li>
    <li>Issue & Return
      <ul>
        <li><a href="#list-2">List</a></li>
        <li><a href="#issue-a-book">Issue a Book</a></li>
        <li><a href="#return-a-book">Return a Book</a></li>
      </ul>
    </li>
  </ul>
</li>
</ul>

# Response structure
Before we dive into APIs, let's look at how the Service responds to requests.<br>
A Response has the following structure:

<table>
  <tr>
    <th colspan=2>Encoding</th>
  </tr>
  <tr>
    <td colspan=2>JSON</td>
  </tr>
  <tr>
    <th colspan=2>Properties</th>
  </tr>
  <tr>
    <td>request</td>
    <td>Code of the request</td>
  </tr>
  <tr>
    <td>code</td>
    <td>Response status code</td>
  </tr>
  <tr>
    <td>payload</td>
    <td>Contains the result of the operation</td>
  </tr>
</table>

<p>In particular: <i>request</i> and <i>code</i> are enumerators:</p>

<table>
  <tr>
    <th>request</th>
    <th>code</th>
  </tr>
  <tr>
    <td>GET_BOOKS</td>
    <td>RESULT_OK</td>
  </tr>
  <tr>
    <td>GET_STUDENTS</td>
    <td>RESULT_ERROR</td>
  </tr>
  <tr>
    <td>GET_REGISTER</td>
    <td>RESULT_BOOK_NOT_EXISTS</td>
  </tr>
  <tr>
    <td>ADD_BOOK</td>
    <td>RESULT_BOOK_ALREADY_EXISTS</td>
  </tr>
  <tr>
    <td>ADD_STUDENT</td>
    <td>RESULT_BOOK_NOT_AVAILABLE</td>
  </tr>
  <tr>
    <td>ISSUE_BOOK</td>
    <td>RESULT_ISBN_NOT_VALID</td>
  </tr>
  <tr>
    <td>DELETE_BOOK</td>
    <td>RESULT_CANT_DELETE_BOOK</td>
  </tr>
  <tr>
    <td>DELETE_STUDENT</td>
    <td>RESULT_QUANTITY_NOT_VALID</td>
  </tr>
  <tr>
    <td>RETURN_BOOK</td>
    <td>RESULT_STUDENT_NOT_EXISTS</td>
  </tr><tr>
    <td>UPDATE_BOOK</td>
    <td>RESULT_STUDENT_ALREADY_EXISTS</td>
  </tr>
  <tr>
    <td>UPDATE_STUDENT</td>
    <td>RESULT_ID_NOT_VALID</td>
  </tr>
  <tr>
    <td>CHECK_BOOK</td>
    <td>RESULT_EMAIL_NOT_VALID</td>
  </tr>
  <tr>
    <td>FIND_BOOK</td>
    <td>RESULT_CANT_DELETE_STUDENT</td>
  </tr>
  <tr>
    <td>FIND_STUDENT</td>
    <td>RESULT_REGISTER_NOT_EXISTS</td>
  </tr>
</table>

# APIs
<h2>Book Management</h2>
<h3>List</h3>
<table>
  <tr>
    <th colspan=2>Request</th>
  </tr>
  <tr>
    <th>Method</th>
    <th>Path</th>
  </tr>
  <tr>
    <td>GET</td>
    <td>/book/list</td>
  </tr>
  <tr>
    <th colspan=2>Parameters</th>
  </tr>
  <tr>
    <td>size</td>
    <td>Result page size</td>
  </tr>
  <tr>
    <td>page</td>
    <td>Result page number</td>
  </tr>
  <tr>
    <th colspan=2>Examples</th>
  </tr>
  <tr>
    <td>/book/list?size=10&page=0</td>
    <td>Books from 0 to 10</td>
  </tr>
  <tr>
    <td>/book/list?size=10&page=2</td>
    <td>Books from 20 to 30</td>
  </tr>
</table>
<table>
  <tr>
    <th colspan=2>Response</th>
  </tr>
  <tr>
    <td>request</td>
    <td>GET_BOOKS</td>
  </tr>
  <tr>
    <td>code</td>
    <td>RESULT_OK</td>
  </tr>
  <tr>
    <td>payload</td>
    <td>Books encoded in JSON</td>
  </tr>
</table>

<h3>Availability Check</h3>
<table>
  <tr>
    <th colspan=2>Request</th>
  </tr>
  <tr>
    <th>Method</th>
    <th>Path</th>
  </tr>
  <tr>
    <td>GET</td>
    <td>/book/check</td>
  </tr>
  <tr>
    <th colspan=2>Parameters</th>
  </tr>
  <tr>
    <td>isbn</td>
    <td>ISBN of the book to check</td>
  </tr>
</table>
<table>
  <tr>
    <th colspan=2>Response</th>
  </tr>
  <tr>
    <td>request</td>
    <td>CHECK_BOOK</td>
  </tr>
  <tr>
    <td rowspan=2>code</td>
    <td>RESULT_OK</td>
  </tr>
  <tr>
    <td>RESULT_BOOK_NOT_EXISTS</td>
  </tr>
  <tr>
    <td>payload</td>
    <td>Number of books available</td>
  </tr>
</table>

<h3>Add</h3>
<table>
  <tr>
    <th colspan=2>Request</th>
  </tr>
  <tr>
    <th>Method</th>
    <th>Path</th>
  </tr>
  <tr>
    <td>POST</td>
    <td>/book</td>
  </tr>
  <tr>
    <th colspan=2>Properties</th>
  </tr>
  <tr>
    <td>Content-Type</td>
    <td>application/json</td>
  </tr>
  <tr>
    <td>Accept</td>
    <td>application/json</td>
  </tr>
  <tr>
    <th colspan=2>Message Body</th>
  </tr>
  <tr>
    <td colspan=2>{"isbn":"0000000000","title":"libro","author":"Andrea","quantity":1}</td>
  </tr>
</table>
<table>
  <tr>
    <th colspan=2>Response</th>
  </tr>
  <tr>
    <td>request</td>
    <td>ADD_BOOK</td>
  </tr>
  <tr>
    <td rowspan=3>code</td>
    <td>RESULT_OK</td>
  </tr>
  <tr>
    <td>RESULT_BOOK_ALREADY_EXISTS</td>
  </tr>
  <tr>
    <td>RESULT_ISBN_NOT_VALID</td>
  </tr>
  <tr>
    <td>payload</td>
    <td>Empty</td>
  </tr>
</table>

<h3>Delete</h3>
<table>
  <tr>
    <th colspan=2>Request</th>
  </tr>
  <tr>
    <th>Method</th>
    <th>Path</th>
  </tr>
  <tr>
    <td>DELETE</td>
    <td>/book</td>
  </tr>
  <tr>
    <th colspan=2>Parameters</th>
  </tr>
  <tr>
    <td>isbn</td>
    <td>ISBN of the book to delete</td>
  </tr>
</table>
<table>
  <tr>
    <th colspan=2>Response</th>
  </tr>
  <tr>
    <td>request</td>
    <td>DELETE_BOOK</td>
  </tr>
  <tr>
    <td rowspan=3>code</td>
    <td>RESULT_OK</td>
  </tr>
  <tr>
    <td>RESULT_BOOK_NOT_EXISTS</td>
  </tr>
  <tr>
    <td>RESULT_CANT_DELETE_BOOK</td>
  </tr>
  <tr>
    <td>payload</td>
    <td>Empty</td>
  </tr>
</table>

<h3>Edit</h3>
<table>
  <tr>
    <th colspan=2>Request</th>
  </tr>
  <tr>
    <th>Method</th>
    <th>Path</th>
  </tr>
  <tr>
    <td>POST</td>
    <td>/book/update</td>
  </tr>
  <tr>
    <th colspan=2>Properties</th>
  </tr>
  <tr>
    <td>Content-Type</td>
    <td>application/json</td>
  </tr>
  <tr>
    <td>Accept</td>
    <td>application/json</td>
  </tr>
  <tr>
    <th colspan=2>Message Body</th>
  </tr>
  <tr>
    <td colspan=2>{"isbn":"0000000000","title":"libro","author":"Andrea","quantity":1}</td>
  </tr>
</table>
<table>
  <tr>
    <th colspan=2>Response</th>
  </tr>
  <tr>
    <td>request</td>
    <td>UPDATE_BOOK</td>
  </tr>
  <tr>
    <td rowspan=3>code</td>
    <td>RESULT_OK</td>
  </tr>
  <tr>
    <td>RESULT_BOOK_NOT_EXISTS</td>
  </tr>
  <tr>
    <td>RESULT_QUANTITY_NOT_VALID</td>
  </tr>
  <tr>
    <td>payload</td>
    <td>Empty</td>
  </tr>
</table>

<h3>Find</h3>
<table>
  <tr>
    <th colspan=2>Request</th>
  </tr>
  <tr>
    <th>Method</th>
    <th>Path</th>
  </tr>
  <tr>
    <td>GET</td>
    <td>/book</td>
  </tr>
  <tr>
    <th colspan=2>Parameters</th>
  </tr>
  <tr>
    <td>isbn</td>
    <td>ISBN of the book to find</td>
  </tr>
</table>
<table>
  <tr>
    <th colspan=2>Response</th>
  </tr>
  <tr>
    <td>request</td>
    <td>FIND_BOOK</td>
  </tr>
  <tr>
    <td rowspan=2>code</td>
    <td>RESULT_OK</td>
  </tr>
  <tr>
    <td>RESULT_BOOK_NOT_EXISTS</td>
  </tr>
  <tr>
    <td>payload</td>
    <td>Book encoded in JSON</td>
  </tr>
</table>

<h2>Student Management</h2>
<h3>List</h3>
<table>
  <tr>
    <th colspan=2>Request</th>
  </tr>
  <tr>
    <th>Method</th>
    <th>Path</th>
  </tr>
  <tr>
    <td>GET</td>
    <td>/student/list</td>
  </tr>
  <tr>
    <th colspan=2>Parameters</th>
  </tr>
  <tr>
    <td>size</td>
    <td>Result page size</td>
  </tr>
  <tr>
    <td>page</td>
    <td>Result page number</td>
  </tr>
  <tr>
    <th colspan=2>Examples</th>
  </tr>
  <tr>
    <td>/student/list?size=10&page=0</td>
    <td>Students from 0 to 10</td>
  </tr>
  <tr>
    <td>/student/list?size=10&page=2</td>
    <td>Students from 20 to 30</td>
  </tr>
</table>
<table>
  <tr>
    <th colspan=2>Response</th>
  </tr>
  <tr>
    <td>request</td>
    <td>GET_STUDENTS</td>
  </tr>
  <tr>
    <td>code</td>
    <td>RESULT_OK</td>
  </tr>
  <tr>
    <td>payload</td>
    <td>Students encoded in JSON</td>
  </tr>
</table>

<h3>Add</h3>
<table>
  <tr>
    <th colspan=2>Request</th>
  </tr>
  <tr>
    <th>Method</th>
    <th>Path</th>
  </tr>
  <tr>
    <td>POST</td>
    <td>/student</td>
  </tr>
  <tr>
    <th colspan=2>Properties</th>
  </tr>
  <tr>
    <td>Content-Type</td>
    <td>application/json</td>
  </tr>
  <tr>
    <td>Accept</td>
    <td>application/json</td>
  </tr>
  <tr>
    <th colspan=2>Message Body</th>
  </tr>
  <tr>
    <td colspan=2>{"id":815564,"name":"Andrea","surname":"Migliore","email":"a.migliore4@studenti.unipi.it"}</td>
  </tr>
</table>
<table>
  <tr>
    <th colspan=2>Response</th>
  </tr>
  <tr>
    <td>request</td>
    <td>ADD_STUDENT</td>
  </tr>
  <tr>
    <td rowspan=4>code</td>
    <td>RESULT_OK</td>
  </tr>
  <tr>
    <td>RESULT_STUDENT_ALREADY_EXISTS</td>
  </tr>
  <tr>
    <td>RESULT_ID_NOT_VALID</td>
  </tr>
  <tr>
    <td>RESULT_EMAIL_NOT_VALID</td>
  </tr>
  <tr>
    <td>payload</td>
    <td>Empty</td>
  </tr>
</table>

<h3>Delete</h3>
<table>
  <tr>
    <th colspan=2>Request</th>
  </tr>
  <tr>
    <th>Method</th>
    <th>Path</th>
  </tr>
  <tr>
    <td>DELETE</td>
    <td>/student</td>
  </tr>
  <tr>
    <th colspan=2>Parameters</th>
  </tr>
  <tr>
    <td>id</td>
    <td>ID of the student to delete</td>
  </tr>
</table>
<table>
  <tr>
    <th colspan=2>Response</th>
  </tr>
  <tr>
    <td>request</td>
    <td>DELETE_STUDENT</td>
  </tr>
  <tr>
    <td rowspan=3>code</td>
    <td>RESULT_OK</td>
  </tr>
  <tr>
    <td>RESULT_STUDENT_NOT_EXISTS</td>
  </tr>
  <tr>
    <td>RESULT_CANT_DELETE_STUDENT</td>
  </tr>
  <tr>
    <td>payload</td>
    <td>Empty</td>
  </tr>
</table>

<h3>Edit</h3>
<table>
  <tr>
    <th colspan=2>Request</th>
  </tr>
  <tr>
    <th>Method</th>
    <th>Path</th>
  </tr>
  <tr>
    <td>POST</td>
    <td>/student/update</td>
  </tr>
  <tr>
    <th colspan=2>Properties</th>
  </tr>
  <tr>
    <td>Content-Type</td>
    <td>application/json</td>
  </tr>
  <tr>
    <td>Accept</td>
    <td>application/json</td>
  </tr>
  <tr>
    <th colspan=2>Message Body</th>
  </tr>
  <tr>
    <td colspan=2>{"id":815564,"name":"Andrea","surname":"Migliore","email":"a.migliore4@studenti.unipi.it"}</td>
  </tr>
</table>
<table>
  <tr>
    <th colspan=2>Response</th>
  </tr>
  <tr>
    <td>request</td>
    <td>UPDATE_STUDENT</td>
  </tr>
  <tr>
    <td rowspan=2>code</td>
    <td>RESULT_OK</td>
  </tr>
  <tr>
    <td>RESULT_STUDENT_NOT_EXISTS</td>
  </tr>
  <tr>
    <td>payload</td>
    <td>Empty</td>
  </tr>
</table>

<h3>Find</h3>
<table>
  <tr>
    <th colspan=2>Request</th>
  </tr>
  <tr>
    <th>Method</th>
    <th>Path</th>
  </tr>
  <tr>
    <td>GET</td>
    <td>/student</td>
  </tr>
  <tr>
    <th colspan=2>Parameters</th>
  </tr>
  <tr>
    <td>id</td>
    <td>ID of the student to find</td>
  </tr>
</table>
<table>
  <tr>
    <th colspan=2>Response</th>
  </tr>
  <tr>
    <td>request</td>
    <td>FIND_STUDENT</td>
  </tr>
  <tr>
    <td rowspan=2>code</td>
    <td>RESULT_OK</td>
  </tr>
  <tr>
    <td>RESULT_STUDENT_NOT_EXISTS</td>
  </tr>
  <tr>
    <td>payload</td>
    <td>Student encoded in JSON</td>
  </tr>
</table>

<h2>Issue & Return</h2>
<h3>List</h3>
<table>
  <tr>
    <th colspan=2>Request</th>
  </tr>
  <tr>
    <th>Method</th>
    <th>Path</th>
  </tr>
  <tr>
    <td>GET</td>
    <td>/register/list</td>
  </tr>
  <tr>
    <th colspan=2>Parameters</th>
  </tr>
  <tr>
    <td>size</td>
    <td>Result page size</td>
  </tr>
  <tr>
    <td>page</td>
    <td>Result page number</td>
  </tr>
</table>
<table>
  <tr>
    <th colspan=2>Response</th>
  </tr>
  <tr>
    <td>request</td>
    <td>GET_REGISTER</td>
  </tr>
  <tr>
    <td>code</td>
    <td>RESULT_OK</td>
  </tr>
  <tr>
    <td>payload</td>
    <td>Issued books encoded in JSON</td>
  </tr>
</table>

<h3>Issue a Book</h3>
<table>
  <tr>
    <th colspan=2>Request</th>
  </tr>
  <tr>
    <th>Method</th>
    <th>Path</th>
  </tr>
  <tr>
    <td>POST</td>
    <td>/register</td>
  </tr>
  <tr>
    <th colspan=2>Properties</th>
  </tr>
  <tr>
    <td>Content-Type</td>
    <td>application/json</td>
  </tr>
  <tr>
    <td>Accept</td>
    <td>application/json</td>
  </tr>
  <tr>
    <th colspan=2>Message Body</th>
  </tr>
  <tr>
    <td colspan=2>{"book_isbn":"0000000000","student_id":815564,"date":"2023-01-21","days":2}</td>
  </tr>
</table>
<table>
  <tr>
    <th colspan=2>Response</th>
  </tr>
  <tr>
    <td>request</td>
    <td>ISSUE_BOOK</td>
  </tr>
  <tr>
    <td rowspan=4>code</td>
    <td>RESULT_OK</td>
  </tr>
  <tr>
    <td>RESULT_BOOK_NOT_EXISTS</td>
  </tr>
  <tr>
    <td>RESULT_BOOK_NOT_AVAILABLE</td>
  </tr>
  <tr>
    <td>RESULT_STUDENT_NOT_EXISTS</td>
  </tr>
  <tr>
    <td>payload</td>
    <td>Issued book data encoded in JSON</td>
  </tr>
</table>

<h3>Return a Book</h3>
<table>
  <tr>
    <th colspan=2>Request</th>
  </tr>
  <tr>
    <th>Method</th>
    <th>Path</th>
  </tr>
  <tr>
    <td>DELETE</td>
    <td>/register</td>
  </tr>
  <tr>
    <th colspan=2>Parameters</th>
  </tr>
  <tr>
    <td>id</td>
    <td>ID of the issued book</td>
  </tr>
</table>
<table>
  <tr>
    <th colspan=2>Response</th>
  </tr>
  <tr>
    <td>request</td>
    <td>RETURN_BOOK</td>
  </tr>
  <tr>
    <td rowspan=2>code</td>
    <td>RESULT_OK</td>
  </tr>
  <tr>
    <td>RESULT_REGISTER_NOT_EXISTS</td>
  </tr>
  <tr>
    <td>payload</td>
    <td>Empty</td>
  </tr>
</table>
