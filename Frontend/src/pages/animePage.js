import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";
import AnimeClient from "../api/animeClient";

class AnimePage extends BaseClass{
    constructor() {
        super();
        this.bindClassMethods(['renderAnimeInfo'], this);
        this.dataStore = new DataStore();
    }

    async mount() {

        this.client = new AnimeClient();
        this.dataStore.addChangeListener(this.renderAnimeInfo);
        this.renderAnimeInfo();

    }


    async renderAnimeInfo(){
        let workArea = document.getElementById("middleBlock");
        const response = await this.client.getAnimeInfo(sessionStorage.getItem("animeCode"), this.errorHandler);


        console.log(response);
        workArea.innerHTML = `
            <div id="container">
            <div id="info-div">
            <h1 id ="title">${response.title}</h1>
            <p id="description">${response.description}</p>
            </div>   
            <div id = "image-box">
            <img src=${response.image}>
            </div>
            </div>
            <div class="extra-info">
            <p> GENRES: ${response.genre} </br>
               EPISODES: ${response.episodes}</br>
               RATING: ${response.rating}
            </p>
            
            </div>
        `

    }


}
const main = async () => {
    const animePage = new AnimePage();
    animePage.mount();
};

window.addEventListener('DOMContentLoaded', main);