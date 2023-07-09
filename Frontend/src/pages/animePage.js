import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";
import AnimeClient from "../api/animeClient";
let lastEvaluatedKey = null;


class AnimePage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['renderAnimeInfo', 'getReviewsFromAnimeID'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        
        document.getElementById("create-form").addEventListener('submit', this.onSubmit)
        this.client = new AnimeClient();
        this.dataStore.addChangeListener(this.renderAnimeInfo);
        this.renderAnimeInfo();

    }


    async renderAnimeInfo() {
        let workArea = document.getElementById("main");
        const response = await this.client.getAnimeInfo(sessionStorage.getItem("animeCode"), this.errorHandler);


        console.log(response);

        if (response.rating == null) {
            response.rating = "N/A";
        } 

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


                    <div class = "info-dos">
                        <div class = "ratings">
                            Ratings
                            <h1>${response.rating}</h1>
                        </div>

                        <div class = "addl-info">
                            <p>
                                <b>Year Released:</b>  ${response.startDate}
                                <br>
                                <b>Genres:</b> ${response.genre}
                                <br>
                                <b>Episodes:</b> ${response.episodes}
                            </p>
                        </div>

                        <div class = "addl-info-2">
                            <p>
                                <b>Popularity #:</b> ${response.popularity}
                                <br>
                                <b>Season:</b> ${response.season}
                                <br>
                                <b>Property:</b> N/A
                            </p>
                        </div>
                    </div>    
                </div>
            </div>
            <div class="reviews">
                <h3>Reviews:</h3>
                <hr>
                <div id = "reviewBox"></div>

                <div id = "create-box" class = "hide">
                    <h3> Have some thoughts? Share them! This is a safe space, trust us! </h3>
                    <button id = "create">Create a Review</button>  
                    
                    <form id = "create-form" class = "hide">
                        <label for = "create-textbox" class = "create-textbox-class">
                        </label>
                        <hr>
                        Rating: <input type = "number" id = "rating-field" min = "0" max = "100" size = "1"></input> / 100

                        <textarea id = "create-textbox" rows = "5" cols = "100" placeholder = "Write something...."></textarea>

                        <button id = "submit-review" type = "submit"> Submit </button>
                    </form>
                </div>
                
                
                <button id = "load">
                    <span> &#10533; Load Reviews &#10534; </span>
                </button>
            </div>
            `

        document.getElementById("load").addEventListener('click', this.getReviewsFromAnimeID);
    }

    async onSubmit(event) {
        event.preventDefault();                 // Prevents the page from refreshing on Submit.

        let createReview = document.getElementById('submit-review');
        createReview.innerText = "Processing...";
        createReview.disabled = true;

        const rating = document.getElementById("rating-field").value;
        const review = document.getElementById("create-textbox").value;

        const createdReview
    }

    async getReviewsFromAnimeID() {
        const response = await this.client.getReviewsByAnimeID(sessionStorage.getItem("animeCode"), lastEvaluatedKey, this.errorHandler);
        if (response[1].length < 9) {
            document.getElementById("load").remove();
        }

        let reviewArea = document.getElementById("reviewBox");
        console.log(reviewArea)
        console.log(response)
        let reviews = `       
        
        `;

        for (let i = 0; i < response[1].length; i++) {
            let month = getMonth(response[1][i].postDate)
            reviews += `
                <div class="user-review">
                <div class="user-profile">
               
                <img class="pfp" src="https://res.cloudinary.com/devbshzwb/image/upload/v1688487914/r8oojdcdeetn66r2ihkp-media_lib_thumb_oplful.jpg"/>
                <a href="userProfilePage.js" class ="display-name">MichaelMichael</a>
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

        document.getElementById("create-box").classList.remove("hide");

        document.getElementById("create").addEventListener("click", () => {
            document.getElementById("create-form").classList.remove("hide"); 
            document.getElementById("create").classList.add("hide");}
        );

    }
}

function getMonth(date) {
    console.log(date)
    let string = parseInt(date.toString().substring(4, 6));
    console.log(string)

    let month;

    switch (string) {
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
    let posted = month + " " + date.toString().substring(6, 8) + ", " + date.toString().substring(0, 4);
    return posted;
}



const main = async () => {
    const animePage = new AnimePage();
    animePage.mount();
};

window.addEventListener('DOMContentLoaded', main);