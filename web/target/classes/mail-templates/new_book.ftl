<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 0;
            background-color: #f5f5f5;
        }
        .header {
            background-color: #333;
            color: white;
            padding: 20px;
            text-align: center;
        }
        .container {
            max-width: 600px;
            margin: 20px auto;
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .footer {
            text-align: center;
            padding: 20px;
            color: #666;
            font-size: 12px;
        }
        .book-info {
            margin: 20px 0;
        }
        .book-info h2 {
            color: #333;
        }
        .rare-edition {
            background-color: #fff3cd;
            border: 1px solid #ffc107;
            padding: 10px;
            border-radius: 4px;
            margin: 10px 0;
            color: #856404;
        }
    </style>
</head>
<body>
    <div class="header">
        <img src="https://raw.githubusercontent.com/spring-projects/spring-boot/main/spring-boot-project/spring-boot-starters/spring-boot-starter-parent/docs/spring-boot-logo.png" alt="Spring Boot Logo" height="40" style="margin-bottom: 10px;">
        <h1>New Book Added</h1>
    </div>
    
    <div class="container">
        <div class="book-info">
            <h2>${title}</h2>
            <p><strong>Author:</strong> ${author}</p>
            <p><strong>Year:</strong> ${year}</p>
            <p><strong>Added:</strong> ${added}</p>
            
            <#-- Safe numeric conversion for year -->
            <#attempt>
                <#assign yearNum = year?number>
            <#recover>
                <#-- If conversion fails, try to clean the string and convert -->
                <#attempt>
                    <#assign yearStr = year?string?replace("а","0")?replace("А","0")?replace("о","0")?replace("О","0")>
                    <#assign yearNum = yearStr?number>
                <#recover>
                    <#-- If all else fails, use default value that won't trigger rare edition -->
                    <#assign yearNum = 9999>
                </#attempt>
            </#attempt>
            
            <#if yearNum < 2000>
                <div class="rare-edition">
                    <strong>Rare edition</strong>
                </div>
            </#if>
        </div>
    </div>
    
    <div class="footer">
        <p>Guestbook Application - Lab 7</p>
    </div>
</body>
</html>

