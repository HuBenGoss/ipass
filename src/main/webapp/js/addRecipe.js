function addIngredient() {

    var itemnumber = $('#ingredients-form .row').last().attr('item-number');
    itemnumber++;

    if(isNaN(itemnumber))
        itemnumber = 1;

    $('#ingredients-form').append(
        "<div class=\"row field\" id=\"ingredient"+itemnumber+"\" item-number='"+itemnumber+"'></div>"
    );
    var row = $('#ingredients-form .row[item-number="' + itemnumber + '"]');

    if(localStorage.getItem("allIngredients") === null) {


        fetch('restservices/ingredients', {method: 'GET'})
            .then(response => response.json())
    .then(function (jsonData) {
            var data = [
                {
                    quantity: 0,
                    options: jsonData,
                    selectedItem: ""
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
                quantity: 0,
                selectedItem: ""

            }
        ];
        row.loadTemplate("templates/ingredient.html",
            data
        );
    }

    return itemnumber;
}

function addStep() {

    var itemnumber = $('#steps-form .row').last().attr('item-number');
    itemnumber++;

    if(isNaN(itemnumber))
        itemnumber = 1;

    $('#steps-form').append(
        "<div class=\"row field\" id=\"step"+itemnumber+"\" item-number='"+itemnumber+"'></div>"
    );
    var row = $('#steps-form .row[item-number="' + itemnumber + '"]');


    row.loadTemplate("templates/step.html");


    return itemnumber;
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
function save() {
    var steps = [];
    var ingredients = [];
    var data = new URLSearchParams();

    $('#steps-form').children('.row').each(function () {
        var description = $(this).children('.description-base').children('.form-group').children('.description-field').val();
        var step = $(this).children('.number-base').children('.form-group').children('.step-field').val();
        var remove = $(this).children('.check-box-base').children('.form-group').children('.form-control').prop('checked');
        if(remove == false)
            steps.push({description: description,step: step});
    });

    $('#ingredients-form').children('.row').each(function () {
        var ingredient = $(this).children('.select-base').children('.form-group').children('select').val();
        var quantity = $(this).children('.number-base').children('.form-group').children('.form-control').val();
        var remove = $(this).children('.check-box-base').children('.form-group').children('.form-control').prop('checked');
        if(remove == false)
            fill(ingredients,ingredient,quantity);
    });

    data.append("title",$('#title').val());
    data.append("cookingTime",$('#cookingTime').val());
    data.append("steps",JSON.stringify(steps));
    data.append("ingredients",JSON.stringify(ingredients));

    fetch("restservices/recipes/add", {method: 'POST',body: data});
    // $.ajax({
    //     url: 'restservices/recipes/add',
    //     data: data,
    //     cache: false,
    //     contentType: false,
    //     processData: false,
    //     method: 'POST',
    //     type: 'POST', // For jQuery < 1.9
    //     success: function(data){
    //         console.log(data);
    //     }
    // });

}