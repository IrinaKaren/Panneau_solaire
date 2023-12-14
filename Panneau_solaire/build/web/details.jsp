<%@page import="model.V_coupure"%>
<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    List<V_coupure> listcoupure = ( List<V_coupure>)request.getAttribute("listcoupure");
    String consommationStr = (String) request.getAttribute("consommation");
    double consommation = Double.parseDouble(consommationStr);
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Details du source solaire</title>
    <style>
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f0f0f0;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .table-container {
            width: 80%;
            background-color: #fff;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            padding: 20px;
            border-radius: 8px;
        }

        h2 {
            color: #333;
            text-align: center;
        }
        
        span {
            margin-left: 500px;
        }

        p {
            margin: 5px 0;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 10px;
        }

        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        th {
            background-color: #f2d7e8;
            color: black;
        }

        tr:hover {
            background-color: #faf7fb;
        }

        .summary {
            margin-top: 15px;
        }

        .summary b {
            color: #555;
        }
  
        .button {
            display: inline-block;
            padding: 10px 20px;
            font-size: 16px;
            text-align: center;
            text-decoration: none;
            background-color: #f2d7e8;
            color: white;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }

        .button:hover {
            background-color: #faf7fb;
        }

    </style>
</head>
<body>
    <div class="table-container">
        <h2>Liste de coupures du source solaire <%= listcoupure.get(0).getSource_solaire() %> le <%= listcoupure.get(0).getJour_semaine() %></h2>
        <table>
            <thead>
                <tr>
                    <th>Date coupure</th>
                    <th>Present matin</th>
                    <th>Present apres-midi</th>                    
                    <th>Consommation</th>
                </tr>
            </thead>
            <tbody>
                <% for(int i = 0; i<listcoupure.size(); i++){ %>
                <tr>
                    <td><%= listcoupure.get(i).getDate_coupure() %></td>
                    <td><%= listcoupure.get(i).getPresent_matin() %></td>
                    <td><%= listcoupure.get(i).getPresent_aprem() %></td>                   
                    <td><%= consommation %></td>
                </tr>
                <% } %>
            </tbody>
        </table>
</body>
</html>
