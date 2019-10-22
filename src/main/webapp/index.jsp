<%--
  Created by IntelliJ IDEA.
  User: Pavel
  Date: 18.10.2019
  Time: 13:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <style>
    <%@include file='resources/table.css' %>
    <%@include file='resources/index.css' %>
  </style>
</head>
<body>
<div class="container">
  <div class="set-form">
    <div class="form">
      <form name="form">
        <div class="set-form__car-number">
          <input type="text" name="carNumber" placeholder="Номер машины" required/>
        </div>
        <div class="set-form__btn">
          <button id="send" type="button" onclick="regCar()">Добавить</button>
          <button id="reset" type="reset">Отмена</button>
        </div>
      </form>
    </div>
    <div class="textarea">
      <textarea id="setCar" readonly></textarea>
    </div>
  </div>
  <div class="filter">
    <div class="form-filter">
      <div class="form-filter__input">
        <form name="filter">
          <input id="filter1" type="text" name="carNumber" placeholder="Номер машины"/>
          <input type="text" name="date" placeholder="Дата YYYYmmDD"/>
        </form>
      </div>
      <div class="form-filter__btn">
        <button type="button" onclick="tableUp()">Получить записи</button>
      </div>
    </div>
    <div class="filter__count">
      <div class="filter__count-btn">
        <button id="count" type="button" onclick="count()">Количество записей</button>
      </div>
      <div class="filter__count-text">
        <span id="textCount">N</span>
      </div>
    </div>
  </div>

  <div id="table" class="scroll-table">

  </div>
</div>
<script>
  <%@include file='resources/index.js' %>
</script>
</body>
</html>
