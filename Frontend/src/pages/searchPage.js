import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import HomePageClient from "../api/homePageClient";
import AnimeClient from "../api/animeClient";

class SearchPage extends BaseClass{

    constructor() {
        super();
        this.bindClassMethods(['renderSearchPage'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        document.getElementById("search-form").addEventListener("submit", this.renderSearchPage);
        this.client = new AnimeClient();
    }

    async renderSearchPage(event){
        event.preventDefault()

        let workArea = document.getElementById("work-area");
        let searchValue = document.getElementById("search-bar").value;
        let searchValueIntoString = `${searchValue}`;

        console.log(searchValue);

        const newResponse = await this.client.getAnimeBySearch(searchValueIntoString, this.errorHandler);

        let items = "";

        console.log(newResponse[0]);
        for(let i = 0; i < newResponse.length; i++){
            items +=`
            <div class="poster">
            <a href="animepage.html"><img class = "poster-click" alt=${newResponse[i].id} src=${newResponse[i].coverImage.large} width="160px" height="90%"></a>  
            <p>${newResponse[i].title.userPreferred}</p>
            </div>
         `;
        }

        const style = document.createElement('style');
        style.innerHTML = `
`;

        document.head.appendChild(style);

        workArea.innerHTML = items;

        document.querySelectorAll(".poster-click").forEach(poster =>{
            poster.addEventListener("click", async function(){
                sessionStorage.setItem("animeCode", poster.alt)
            });
        });
    }

}

const main = async () =>{
    const searchPage = new SearchPage();
    searchPage.mount();
    console.log("mount");
};

window.addEventListener('DOMContentLoaded', main);