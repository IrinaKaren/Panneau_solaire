<%@page import="java.sql.Timestamp"%>
<%@page import="java.util.List"%>
<%@ page contentType="text/html; charset=UTF-8" %>
<%
    double[] listBesoinEstim = (double[])request.getAttribute("listBesoinEstim");
    Timestamp[] listDateCoupureEstim = (Timestamp[])request.getAttribute("listDateCoupureEstim");
    String[] listSourceSolaire =(String[]) request.getAttribute("listSourceSolaire");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Etat de stock</title>
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
        <h2>Liste de coupures des sources solaire</h2>
        <table>
            <thead>
                <tr>
                    <th>Source solaire</th>
                    <th>Date coupure</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
            <% for (int i = 0; i < listDateCoupureEstim.length; i++) { %>
                 <tr>
                     <td><%= listSourceSolaire[i] %></td>
                     <td><%= listDateCoupureEstim[i] %></td>
                     <td><a class="button" href="DetailsController?consommation=<%= listBesoinEstim[i] %>&source_solaire=<%= listSourceSolaire[i] %>&date_coupure=<%= listDateCoupureEstim[i] %>" style="color: black;">Details</a></td>
                 </tr>
             <% } %>

            </tbody>
        </table>
</body>
</html>
