<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Formulaire entrée date</title>
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }

        .form-container {
            width: 300px;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        .form-container h1{
            text-align: center;
        }
        
        .form-container label {
            display: block;
            margin-bottom: 8px;
        }

        .form-container select,
        .form-container input {
            width: 100%;
            padding: 8px;
            margin-bottom: 16px;
            box-sizing: border-box;
        }

        .form-container button {
            background-color: #f2d7e8;
            color: black;
            padding: 10px 40px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        .form-container button:hover {
            background-color: #d8bbd3;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <h1>Formulaire estimation coupure à:</h1>
        <form action="FormController" method="get">   
            <label for="date">Date:</label>
            <input type="date" id="date" name="date" required>
            <button type="submit">Valider</button>
        </form>
    </div>
</body>
</html>

