<%@ tag description="Writes the HTML code for output managements menu." %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="creating_form">
<form action="" method="post">
    <fieldset>
        <legend>Creating product</legend>
        <label class="creating_form_input">Select category:
            <select>
                <option value="sydney">Sydney</option>
                <option value="melbourne">Melbourne</option>
                <option value="cromwell">Cromwell</option>
                <option value="queenstown">Queenstown</option>
            </select>
        </label>
        <label class="creating_form_input">Name:
            <input type="text" name="first_name" value="" maxlength="100" class="product_name_input"/>
        </label>
        <label class="creating_form_input">Price:
            <input type="text" name="first_name" value="" maxlength="100"/>
        </label>

        <div class="creating_form_input">
            <label>Description:<br>
                <textarea rows="3" cols="87" name="description"></textarea>
            </label>
        </div>

        <div class="creating_form_input">
            <label>Attributes:<br></label>
            <table class="attributes_table">
                <tr>
                    <th>Attribute name</th>
                    <th>Value</th>
                    <th>Delete</th>
                </tr>
                <tr>
                    <th colspan="2"><span><button type="submit" class="myButton">Add attribute</button></span></th>
                    <th><span><button type="submit" class="myButton">Delete selected</button></span></th>
                </tr>
            </table>
        </div>
    </fieldset>
</form>
</div>


