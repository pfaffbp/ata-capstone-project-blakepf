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
        let workArea = document.getElementById("main");
        const response = await this.client.getAnimeInfo(sessionStorage.getItem("animeCode"), this.errorHandler);


        console.log(response);
        workArea.innerHTML = `
            <div class = "image-box">
                    <img src= ${response.image}>

                    <div class = "anime-links">
                        <a href = "holder"> + Add to Favorites + </a>
                    </div>

                </div>

                <div class = "anime-info">
                    <h1> ${response.title} </h1>
                    <hr>
                    <p id = "description">
                        ${response.description}
                    </p>

                    <div class = "ratings">
                        Ratings
                        <h1>88</h1>
                    </div>

                    <div class = "addl-info">
                        <p>
                            <b>Start Date:</b> N/A
                            <br>
                            <b>Genres:</b> ${response.genre}
                            <br>
                            <b>Episodes:</b> ${response.episodes}
                        </p>
                    </div>

                    <div class = "addl-info-2">
                        <p>
                            <b>Popularity #:</b> N/A
                            <br>
                            <b>Seasons:</b> N/A
                            <br>
                            <b>Property:</b> N/A
                        </p>
                    </div>
                </div>
            </div>

            <div class = "reviews">
                <h3>Reviews:</h3>
                <hr>
                <button = "load">
                    <span> 
                        &#10533; Load Reviews &#10534; 
                    </span>
                </button>
            </div>

            <!-- <div class = "reccomended-anime">
                <h3> If you like ${response.title}, check these out: </h3>
                <hr>
            </div> -->
        
        `
    }


}
const main = async () => {
    const animePage = new AnimePage();
    animePage.mount();
};

window.addEventListener('DOMContentLoaded', main);