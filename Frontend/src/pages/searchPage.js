import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import HomePageClient from "../api/homePageClient";

class SearchPage extends BaseClass{

    constructor() {
        super();
        this.bindClassMethods(['renderSearchPage'], this);
        this.dataStore = new DataStore();
    }

    async mount() {

        this.dataStore.addChangeListener(this.renderSearchPage)
        this.client = new HomePageClient();
        this.renderSearchPage();
    }

    async renderSearchPage(){

        let workArea = document.getElementById("work-area");



    }

}

const main = async () =>{
    const searchPage = new SearchPage();
    searchPage.mount();
    console.log("mount");
};

window.addEventListener('DOMContentLoaded', main);