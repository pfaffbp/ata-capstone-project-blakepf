import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ApiClient from "../api/apiClient";
const argMap = new Map();
const fieldMap = new Map();

class ApiPage extends BaseClass{

    constructor() {
        super();
        this.bindClassMethods(['createApiCall', 'renderProperties', 'finalCall'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        document.getElementById('clientCalls').addEventListener('submit', this.createApiCall);
        document.getElementById('apiCall').addEventListener('submit', this.finalCall);

        this.client = new ApiClient();
    }


    async createApiCall(event){
        event.preventDefault();

        let argumentValue = document.getElementById("Arguments").value;
        let fieldValue = document.getElementById("Fields").value;
        let pageOrNot = document.getElementById("Pagination").value;



        this.dataStore.set("Pagination", pageOrNot);
        this.dataStore.set("Arguments", argumentValue);
        this.dataStore.set("Fields", fieldValue);


        this.renderProperties();

        console.log(argumentValue, fieldValue, pageOrNot);
    }

    //  renderProperties(){
    //     const workArea = document.getElementById("apiCall");
    //     console.log("called");
    //     console.log(workArea)
    //     let area = `<div class = "container">
    //                 `
    //
    //
    //     if(this.dataStore.get("Pagination") === true){
    //         area += `
    //             <div class = "Page-Arguments">
    //             <label for = "Page-Arguments-Number">
    //             <h3>Page Number</h3>
    //             </label>
    //             <input type = number id ="Page-number">
    //             </div>
    //        `
    //     }
    //
    //     for (let i = 0; i < this.dataStore.get("Arguments"); i++) {
    //         area += `<div class = "arg-container">
    //             <label for="Argument-Media">
    //             <h3>Argument field</h3>
    //             </label>
    //             <input type="text" id="Argument-Media">
    //             <label for="Argument-Media-Param">
    //             <h3>Argument Param</h3>
    //             </label>
    //              <input type="text" id="Argument-Media-Param">
    //             </div>
    //         `
    //     }
    //
    //     for (let i = 0; i < this.dataStore.get("Fields"); i++) {
    //         area += `<div class = "field-container">
    //             <label for="Field">
    //             <h3>Field</h3>
    //             </label>
    //             <input type="text" id="Field">
    //             <label for="Field-Arg">
    //             <h3>Field Param</h3>
    //             </label>
    //              <input type="text" id="Field-Arg">
    //             </div>
    //         `
    //     }
    //     area+= `</div>
    //             <button id="finish-button">Api call</button>
    //             `
    //      console.log(area);
    //     workArea.innerHTML = area;
    // }

    renderProperties(){
        const workArea = document.getElementById("apiCall");
        console.log("called");
        console.log(workArea)
        let area = `<div class = "container">
                    `


        if(this.dataStore.get("Pagination") === true){
            area += `
                <div class = "Page-Arguments">
                <label for = "Page-Arguments-Number">
                <h3>Page Number</h3>
                </label>
                <input type = number id ="Page-number">
                </div>
           `
        }

        for (let i = 0; i < this.dataStore.get("Arguments"); i++) {
            area += `<div class = "arg-container">
                <label for="Argument-Media">
                <h3>Argument field</h3>
                </label>
                <input type="text" class="Argument-Media">
                <label for="Argument-Media-Param">
                <h3>Argument Param</h3>
                </label>
                 <input type="text" class="Argument-Media-Param">
                </div>
            `
        }

        for (let i = 0; i < this.dataStore.get("Fields"); i++) {
            area += `<div class = "field-container">
                <label for="Field">
                <h3>Field</h3>
                </label>
                <input type="text" class="Field">
                <label for="Field-Arg">
                <h3>Field Param</h3>
                </label>
                 <input type="text" class="Field-Arg">
                </div>
            `
        }
        area+= `</div>
                <button id="finish-button">Api call</button>
                `
        console.log(area);
        workArea.innerHTML = area;
    }

    // async finalCall(event){
    //     event.preventDefault();
    //     console.log("final call log")
    //
    //
    //
    //     let arg = document.getElementById("apiCall");
    //
    //     // console.log(args.values().next())
    //
    //     for (let i = 0; i < arg.textContent.length; ) {
    //         if(arg[i] === document.getElementById("Argument-Media")){
    //             argMap.set(arg[i], arg[i + 1]);
    //         }
    //
    //         if(arg[i] === document.getElementById("Field")){
    //             console.log("Field");
    //             fieldMap.set(arg[i].value, arg[i + 1].value);
    //         }
    //         console.log(arg[i]);
    //         i+=2;
    //
    //     }
    //     console.log(fieldMap.keys());
    //     console.log(argMap.entries());
    // }

    async finalCall(event){
        event.preventDefault();
        console.log("final call log")



        let field = document.querySelectorAll(".Field");
        let fieldArg = document.querySelectorAll(".Field-Arg");
        let arg = document.querySelectorAll(".Argument-Media")
        let argParam = document.querySelectorAll(".Argument-Media-Param")
        let pagination = document.getElementById("Pagination").value;

        for (let i = 0; i < field.length; i++) {
            fieldMap.set(field[i].value, fieldArg[i].value);
        }

        for (let i = 0; i < arg.length; i++) {
            argMap.set(arg[i].value, argParam[i].value);
        }

        let result = await this.client.getExample(querySetup(pagination, argMap, fieldMap), this.errorHandler);

        // await this.client.createExample(result, this.errorHandler);

        console.log(result);
        console.log("here")

    }

}
function querySetup(pageOrNot, argMap, fieldMap){
    let query = ``;
    let intBraces = 0;
    console.log(pageOrNot);
    if(pageOrNot){
        query += `{
            Page(page : 1){
            `
        intBraces += 2;
    }

    let index = 0;
    for(let [key, value] of argMap.entries()){
        if(index === 0){
            query+= `media(${key} : ${value}`
            console.log("in if")
        } else{
            query += `,${key} : ${value}`
        }
        index+=1;
    }
    query+= `){
`
    intBraces+=1;

    for(let[key, value] of fieldMap.entries()){
        if(value === ""){
            query+= `${key}
  `
        } else{
            query+= `${key}{
     ${value}
}
`
        }
    }
    for (let i = 0; i < intBraces; i++) {
        query += `}`
    }

    return query;
}



const main = async () => {
    console.log("mounted")
    const apiPage = new ApiPage();
    await apiPage.mount();

};

window.addEventListener('DOMContentLoaded', main);