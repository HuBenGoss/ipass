function addRecipe(title,cookingTime,id) {
    var data = [
        {
            image: "http://via.placeholder.com/510x300",
            imagealt: title,
            title: title,
            cookingTime: cookingTime,
            pagelink:"",
            urltext:"verder lezen →",
            pagelink:"/recipe.html?id="+id
        }
    ];

    $('.recipe-list').append("<div class=\"post\" id='post-"+id+"'></div>");

    $("#post-"+id).loadTemplate("templates/recipe.html",data);





}

function loadRecipes() {
    console.log("loading Recipes");
    $('.recipe-list').children('.post').each(function () {
       $(this).remove();
    });
    var encData = new URLSearchParams();
    data = localStorage.getItem("ingredients");

    encData.append("ingredients",data)

    var url = new URL("http://localhost:8090/restservices/recipes/recipe/");
    url.search = encData;
    fetch(url)
        .then(response => response.json())
        .then(function (jsonData) {
            jsonData.forEach(function (item) {
                addRecipe(item.title,item.cookingTime,item.id);
            });
        });

    console.log("Finished loading");

}