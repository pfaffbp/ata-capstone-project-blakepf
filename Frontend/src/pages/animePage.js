import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";
import AnimeClient from "../api/animeClient";
let lastEvaluatedKey = null;
let displayName = localStorage.getItem('displayName')


class AnimePage extends BaseClass {
    constructor() {
        super();
        this.bindClassMethods(['Logout', 'onLoad', 'renderAnimeInfo', 'onSubmit', 'getReviewsFromAnimeID', 'addToFavorites', 'getNotifications'], this);
        this.dataStore = new DataStore();
    }

    async mount() {
        this.client = new AnimeClient();
        this.dataStore.addChangeListener(this.renderAnimeInfo);
        this.renderAnimeInfo();
        document.getElementById('logout').addEventListener('click', this.Logout);
        this.onLoad();
    }

    async Logout(event) {
        event.preventDefault();
        localStorage.clear();
        window.location.href = "login.html";
    }

    async onLoad() {
        let user = localStorage.getItem('displayName')

        let LoggedInArea = document.getElementById('userLoggedIn');
        if (user != null) {
            document.getElementById("bell").classList.remove("hide");
            document.getElementById("bell").addEventListener("click", this.getNotifications);
            LoggedInArea.innerHTML = user;
        } else
            LoggedInArea.innerHTML = "Login";

        const response = await this.client.getNotifications(user, this.errorHandler());
        console.log(response);
    }


    async renderAnimeInfo() {
        let workArea = document.getElementById("main");
        const response = await this.client.getAnimeInfo(sessionStorage.getItem("animeCode"), this.errorHandler);
        const getRating = await this.client.getRatingForAnime(sessionStorage.getItem("animeCode"), this.errorHandler);

        console.log(response);

        if (response.rating == null) {
            response.rating = "N/A";
        }

        workArea.innerHTML += `
            <div class = "image-box">
                <img src = "${response.image}">

                <button id = "add-to-fav">+ Add to Favorites +</button>
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
                            </p>
                        </div>

                        <div class = "addl-info-2">
                            <p>
                                <b>Popularity #:</b> ${response.popularity}
                                <br>
                                <b>Season:</b> ${response.season}
                                <br>
                                <b>Episodes:</b> ${response.episodes}
                            </p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="reviews">
                <h3>Reviews:</h3>
                <hr>

                <div id = "create-box" class = "hide">
                    <h3 id = "title-create"> Have some thoughts of your own? Share them! </h3>
                    <h4 id = "title-create-2"><i> This is a safe space, trust us! </i></h4>
                    <button id = "create">Create a Review</button>

                    <form id = "create-form" class = "hide">
                        <label for = "create-textbox" class = "create-textbox-class">
                        </label>
                        <hr>
                        Rating: <input type = "number" id = "rating-field" min = "0" max = "100" size = "1"></input> / 100

                        <textarea id = "create-textbox" rows = "5" cols = "100" placeholder = "Write something...." oninput = "limitChar(this)" maxlength = "400"></textarea>
                        <span id = "charCounter">400 character limit!</span>

                        <button id = "submit-review" type = "submit"> Submit </button>
                    </form>

                    <div id = "post-submit" class = "hide">
                        <h3> Just submitted your beautiful, hand-crafted review! </h3>
                        <h4> Click 'Refresh' below to check it out! </h4>
                        <button id = "refresh" type = "click">Refresh</button>
                    </div>
                </div>

                <div id = "reviewBox"></div>
                <button id = "load">
                    <span> &#10533; Load Reviews &#10534; </span>
                </button>
            </div>
            `

        document.getElementById("load").addEventListener('click', this.getReviewsFromAnimeID);
        document.getElementById("create-form").addEventListener('submit', this.onSubmit);
        document.getElementById("add-to-fav").addEventListener("click", this.addToFavorites);
    }

    async onSubmit(event) {
        event.preventDefault();                 // Prevents the page from refreshing on Submit.

        let createReview = document.getElementById('submit-review');
        createReview.innerText = "Processing...";
        createReview.disabled = true;

        const rating = document.getElementById("rating-field").value;
        const review = document.getElementById("create-textbox").value;

        await this.client.postReview(review, sessionStorage.getItem("animeCode"), "string", rating, this.errorHandler)

        document.getElementById("create-form").classList.add("hide");
        document.getElementById("title-create").classList.add("hide");
        document.getElementById("title-create-2").classList.add("hide");
        document.getElementById("post-submit").classList.remove("hide");

        document.getElementById("refresh").addEventListener("click", () => window.location.reload())

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
               
                </div>
                <div class="middleBlock">
                <a href="userProfilePage.js" class ="display-name">${response[1][i].displayName}</a>
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
            document.getElementById("create").classList.add("hide");
        }
        );

    }

    async addToFavorites() {
        console.log("In the addToFavorites method!");

        let displayName = localStorage.getItem("displayName");
        console.log(displayName);

        const animeId = sessionStorage.getItem("animeCode");
        console.log(animeId);

        const animeInfo = await this.client.getAnimeInfo(animeId);

        const response = await this.client.addToFavorites(displayName, animeId, this.errorHandler);
        console.log(response)                                               // On submit, checking to see what the response is.

        let notificationRequest = " added " + animeInfo.title + " to their Favorites List!";
        console.log(notificationRequest);

        console.log("hi")

        let userFollowers = await this.client.getUserData(displayName);
        console.log(userFollowers);

        if (userFollowers.followers != null) {
            userFollowers = userFollowers.followers;
            console.log(userFollowers);

            for (let i = 0; i < userFollowers.length; i++) {
                console.log("In the forEach of setNotifications");                                                                                   // Check to see if we're in the method.
                const response = await this.client.setNotification(userFollowers[i], notificationRequest, displayName, this.errorHandler);
                console.log(response);                                                                                                                // Check to see if it returns a proper response.
            }
        } else {
            console.log("No followers to send notifications to!");
        }

    }

    async getNotifications() {
        console.log("In getNotifications");                                 // Checks to see if it makes it to this method when clicking the bell.
        let user = localStorage.getItem("displayName");                     // Grabbing the user name.
        console.log(user);                                                  // Logging the user name to check and see if it pulls the correct one.

        const response = await this.client.getNotifications(localStorage.getItem("displayName"), this.errorHandler)         // Sends a get request to the Lambda API.
        console.log(response);
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