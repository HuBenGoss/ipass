function getIngredients() {
    array = [];

    return array;
}

function addField(selectedIngredient,quantity) {

    var itemnumber = $('.form .row').last().attr('item-number');
    itemnumber++;

    if(isNaN(itemnumber))
        itemnumber = 1;

    $('.form').append(
        "<div class=\"row field\" id=\"item"+itemnumber+"\" item-number='"+itemnumber+"'></div>"
    );
    var row = $('.row[item-number="' + itemnumber + '"]');

    if(localStorage.getItem("allIngredients") === null) {


        fetch('restservices/ingredients', {method: 'GET'})
            .then(response => response.json())
            .then(function (jsonData) {
            var data = [
                {
                    options: jsonData,
                    quantity: quantity,
                    selectedItem: selectedIngredient

                }
            ];
            row.loadTemplate("templates/ingredient.html",
                data
            );
            localStorage.setItem("allIngredients",JSON.stringify(jsonData));
        });
    }else{
        var data = [
            {
                options: JSON.parse(localStorage.getItem("allIngredients")),
                quantity: quantity,
                selectedItem: selectedIngredient

            }
        ];
        row.loadTemplate("templates/ingredient.html",
            data
        );
    }

    return itemnumber;
}
function removeField(itemnumber) {
    $('.row[item-number='+itemnumber+']').remove();
}

function saveToIngredientStock() {

    var ingredients = [];
    $('.form').children('.row').each(function () {
        var ingredient = $(this).children('.select-base').children('.form-group').children('select').val();
        var quantity = $(this).children('.number-base').children('.form-group').children('.form-control').val();
        var remove = $(this).children('.check-box-base').children('.form-group').children('.form-control').prop('checked');

        if(remove == false) {
            ingredients = fill(ingredients, ingredient, quantity);
        }else{
            removeField($(this).attr("item-number"));
        }
    });
    localStorage.setItem("ingredients",JSON.stringify(ingredients));
    loadRecipes();
}

function fill(array,search,value) {
    array.forEach(function (item,i) {
        if(item.ingredient != null && item.quantity != null) {


            if (item.ingredient == search) {
                value = (parseFloat(value) + parseFloat(item.quantity));
                array.splice(i,1);
            }
        }
    });
    array.push({'ingredient':search,'quantity':value});
    return array;
}


function loadIngredients() {
    var ingredients = JSON.parse(localStorage.getItem("ingredients"));
    if(ingredients != null)
    {
        if(ingredients.length > 0){
            ingredients.forEach(function (ingredient) {
                addField(ingredient.ingredient, ingredient.quantity);
            });
        }
        else{
            addField(null,0.01);
        }
    }else{
        addField(null,0.01);
    }

    var removeButton = $('button.remove-Field');
    removeButton.click(function () {
            removeField($(this).parent().parent().attr('item-number'));
        }
    );
}