let checkboxes;
let multipleSel;
let buttons;

function init(){
    checkboxes = document.getElementsByName("checkbox");
    multipleSel = document.getElementById("checkUncheck");
    buttons = document.getElementsByName("button");
}

function checkCheckboxes(){
    init();
    multipleSel.checked = isEveryChecked();
    if(isIncludeSelected()){
        switchButton(false)
    }
    else switchButton();
}

function isEveryChecked(){
    for(let checkbox of checkboxes){
        if(checkbox.checked === false){
            return false;
        }
    }
    return true;
}

function isIncludeSelected(){
    for(let checkbox of checkboxes){
        if(checkbox.checked){
            return true;
        }
    }
    return false;
}

function switchButton(condition = true){
    buttons.forEach((button) => {
        button.disabled = condition;
    });
}

function checkAll(){
    init();
    if(multipleSel.checked){
        check();
    }
    else check(false);
}

function check(checked = true) {
    checkboxes.forEach((checkbox) => {
        checkbox.checked = checked;
    });
}