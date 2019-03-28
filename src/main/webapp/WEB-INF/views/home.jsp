<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>Hello React</title>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
    <script src="https://fb.me/react-0.13.3.js"></script>
    <script src="https://fb.me/JSXTransformer-0.13.3.js"></script>
  </head>
  <body>
    <div id="example"></div>
    <script type="text/jsx">
		// sample01.js
		/*var HelloWorld = React.createClass({
 			render: function() {
    			return (
      				<p>
        				안녕, <input type="text" placeholder="이름을 여기에 작성하세요" />!
        				지금 시간은 {this.props.date.toTimeString()} 입니다.
      				</p>
    			);
  			}
		});
		setInterval(function() {
  			React.render(
    			<HelloWorld date={new Date()} />,
    			document.getElementById('example')
  			);
		}, 500);*/

		// tutorial1.js
		var CommentBox = React.createClass({
  			render: function() {
    			return (
      				<div className="commentBox"> Hello, world! I am a CommentBox. </div>
    			);
  			}
		});
		React.render( <CommentBox />, document.getElementById('content') );
    </script>
  </body>
</html>
