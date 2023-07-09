import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";
import AnimeClient from "../api/animeClient";
let lastEvaluatedKey = null;


class AnimePage extends BaseClass{
    constructor() {
        super();
        this.bindClassMethods(['renderAnimeInfo', 'getReviewsFromAnimeID', 'notification'], this);
        this.dataStore = new DataStore();
    }

    async mount() {

        this.client = new AnimeClient();
        this.dataStore.addChangeListener(this.renderAnimeInfo);
        this.renderAnimeInfo();
        this.notification();

    }


    async renderAnimeInfo(){
        let workArea = document.getElementById("main");
        const response = await this.client.getAnimeInfo(sessionStorage.getItem("animeCode"), this.errorHandler);
        const getRating = await this.client.getRatingForAnime(sessionStorage.getItem("animeCode"), this.errorHandler);

        console.log(response);
        workArea.innerHTML += `
            <div class = "image-box">
                <img src = "${response.image}">
            </div>

                <div class = "anime-info">
                    <h1> ${response.title} </h1>
                    <hr>
                    <p id = "description">
                        ${response.description}
                    </p>

                    <div class = "ratings">
                        Ratings
                        <h1>${getRating}</h1>
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
                <div id ="reviewBox"></div>
                <button id = "load">
                    <span> 
                        &#10533; Load Reviews &#10534; 
                    </span>
                </button>
            </div>
            <div id ="reviewBox"></div>
            <!-- <div class = "reccomended-anime">
                <h3> If you like , check these out: </h3>
                <hr>
            </div> -->
        
            `
        document.getElementById("load").addEventListener('click', this.getReviewsFromAnimeID);
    }

    async getReviewsFromAnimeID(){
        const response = await this.client.getReviewsByAnimeID(sessionStorage.getItem("animeCode"), lastEvaluatedKey, this.errorHandler);
        if(response[1].length < 9){
            document.getElementById("load").remove();
        }

        let reviewArea = document.getElementById("reviewBox");
        console.log(reviewArea)
        console.log(response)
        let reviews = "";
        for(let i = 0; i < response[1].length; i++){
            let month = getMonth(response[1][i].postDate)
            reviews +=`
                <div class="user-review">
                <div class="user-profile">
               
                <img class="pfp" src="https://res.cloudinary.com/devbshzwb/image/upload/v1688487914/r8oojdcdeetn66r2ihkp-media_lib_thumb_oplful.jpg"/>
                <a href="userProfilePage.js" class ="display-name">${response[1][i].displayName}</a>
                </div>
                <div class="middleBlock">
                <p class="comment">${response[1][i].review}</p>
                <div class = "misc">
                <button class="like-button">
                    <img class="like-button-img" src="https://res.cloudinary.com/devbshzwb/image/upload/v1688495058/thumbs_up_dggckt.png"
                            
                    </img>   
                </button>
                <p class="like-number">1000</p> 
                <button class="dislike-button">
                    <img class="dislike-button-img" src="https://res.cloudinary.com/devbshzwb/image/upload/v1688495063/thumbs_down_cbadvq.png"</img> 
                    </button>
                    <h5 class="postedDate">Posted: ${month}</h5>  
                </div>
                </div>
                <div class="rating-box">
                <h4>Rating</h4>
                <p class="rating">${response[1][i].rating}</p>
                </div>
                </div>
                `;
        }
        reviewArea.innerHTML += reviews
        lastEvaluatedKey = response[2];
        console.log(response[1]);
        console.log(lastEvaluatedKey);
    }

    async notification(){
        let response = await this.client.getNotification("testMike", this.errorHandler)

        alert(response);
        setInterval(this.notification, 5000);
    }

}

function getMonth(date){
    console.log(date)
    let string = parseInt(date.toString().substring(4, 6));
    console.log(string)

    let month;

    switch(string){
        case 1:
            month = "January";
            break;
        case 2:
            month = "February";
            break;
        case 3:
            month = "March";
            break;
        case 4:
            month = "April";
            break;
        case 5:
            month = "May";
            break;
        case 6:
            month = "June";
            break;
        case 7:
            month = "July";
            break;
        case 8:
            month = "August";
            break;
        case 9:
            month = "September";
            break;
        case 10:
            month = "October";
            break;
        case 11:
            month = "November";
            break;
        case 12:
            month = "December";
            break;
        default:
            break;
    }
    let posted = month + " " + date.toString().substring(6, 8) + ", " + date.toString().substring(0,4);
    return posted;
}


const main = async () => {
    const animePage = new AnimePage();
    animePage.mount();
};

window.addEventListener('DOMContentLoaded', main);