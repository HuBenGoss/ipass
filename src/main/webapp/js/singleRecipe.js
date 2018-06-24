function addRecipe(jsonData) {

    ingredientHtml = "";
    stepHtml = "";

    $.each(jsonData.ingredients, function (index,ingredient) {
        var measuringUnit = ingredient.measuringUnit;
        if(measuringUnit == null)
            measuringUnit = "";

        var row =  '<li id="ingredient-'+index+'">'+ingredient.quantity+''+measuringUnit+' '+ingredient.name+'</li>';
        ingredientHtml += row;
    });
    $.each(jsonData.steps, function (index,step) {
        var row =  '<li id="step-'+step.stepnr+'">'+step.description+'</li>';
        stepHtml += row;
    });


    var data = [
        {
            image: "http://via.placeholder.com/510x300",
            imagealt: jsonData.title,
            title: jsonData.title,
            ingredients: ingredientHtml,
            steps:stepHtml
        }
    ];

    $("#theRecipe").loadTemplate("templates/singleRecipe.html",data);


}

function loadRecipe() {
    var $_GET = {};

    document.location.search.replace(/\??(?:([^=]+)=([^&]*)&?)/g, function () {
        function decode(s) {
            return decodeURIComponent(s.split("+").join(" "));
        }

        $_GET[decode(arguments[1])] = decode(arguments[2]);
    });
    console.log("loading Recipe");



    var url = new URL("http://localhost:8090/restservices/recipes/recipe/" + $_GET["id"]);

    fetch(url)
        .then(response => response.json())
        .then(function (jsonData) {
            $.when(addRecipe(jsonData[0]));
            localStorage.setItem("recipe-"+$_GET["id"],JSON.stringify(jsonData[0]));
    });

    console.log("Finished loading");

}