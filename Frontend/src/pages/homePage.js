import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import HomePageClient from "../api/homePageClient";
const map = new Map;

class HomePage extends BaseClass {

    constructor() {
        super();
        this.bindClassMethods(['renderHomePage'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        this.dataStore.addChangeListener(this.renderHomePage)
        this.client = new HomePageClient();
        this.renderHomePage();
    }

    async renderHomePage() {
        let workArea = document.getElementById('popular-anime-container');
        let seasonAnime = document.getElementById("seasonal-anime-container");
        let highlyRatedAnime = document.getElementById("highly-rated-container");

        let response = await this.client.getFrontPage(this.errorHandler);

        let items = "";
        let seasonalItems = "";
        let highRatedItems = "";
        console.log(response)


        for(let i =0 ; i < 10; i++){
            console.log("Inside here")
            items += `
         <div class="anime-poster-class">
             <div class="anime-poster">
              <a href="animepage.html"><img class = "poster-click" alt=${response[i].animeId} src=${response[i].image} width="160px" height="90%"></a>
                 <div class="anime-description">
                     <h5>${response[i].title}</h5>
                 </div>
             </div>
         </div>
            `;

        }

        workArea.innerHTML = items;

        for(let i = 10 ; i < 20; i++){
            console.log("Inside here")
            seasonalItems += `
         <div class="anime-poster-class">
             <div class="anime-poster">
              <a href="animepage.html"><img class = "poster-click" alt=${response[i].animeId} src=${response[i].image} width="160px" height="90%"></a>
                 <div class="anime-description">
                     <h5>${response[i].title}</h5>
                 </div>
             </div>
         </div>
            `;}
        seasonAnime.innerHTML = seasonalItems;

        for(let i = 20 ; i < 30; i++){
            console.log("Inside here")
            highRatedItems += `
         <div class="anime-poster-class">
             <div class="anime-poster">
             <a href="animepage.html"><img class = "poster-click" alt=${response[i].animeId} src=${response[i].image} width="160px" height="90%"></a>
                 <div class="anime-description">
                     <h5>${response[i].title}</h5>
                 </div>
             </div>
         </div>
            `;
        }

        highlyRatedAnime.innerHTML = highRatedItems;

        let docs = document.querySelectorAll('.poster-click');

        docs.forEach(href => {
            href.addEventListener('click', async function (){
                // datasset("animeCode", href.value);
                sessionStorage.setItem("animeCode", href.alt);
            });
        });
        await console.log(sessionStorage.getItem("animeCode")); // stores the animeId to get data;
        console.log(docs);
    }

}


    const main = async () =>{
        const homePage = new HomePage();
        homePage.mount();
        console.log("mount");
    };

window.addEventListener('DOMContentLoaded', main);
