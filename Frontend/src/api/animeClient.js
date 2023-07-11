import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";
import axios from "axios";


/**
 * Logic needed for the view playlist page of the website.
 */
export default class AnimeClient extends BaseClass {

    constructor(props = {}) {
        super();
        const methodsToBind = ['clientLoaded', 'getAnimeInfo','getAnimeBySearch', 'getAnimeByGenre', 'uploadAnimeToDatabase', 'postReview', 'getReviewsByAnimeID', 'getRatingForAnime', 'getUserData', 'addToFavorites', 'getNotifications', 'setNotification', 'handleError'];
        this.bindClassMethods(methodsToBind, this);
        this.props = props;
        this.clientLoaded(axios);
    }


    clientLoaded(client) {
        this.client = client;
        if (this.props.hasOwnProperty("onReady")) {
            this.props.onReady();
        }
    }

    async getAnimeInfo(id, errorCallback) {
        try {
            const response = await this.client.get(`/anime/${id}`)
            return response.data;
        } catch (error) {
            this.handleError("getAnimeInfo", error, errorCallback);
        }
    }

    async getAnimeBySearch(title, errorCallback) {
        const map = new Map();
        let query = `
    query findPopularAnime($title: String) {
      Page(page: 1, perPage: 50) {
        media(search: $title, type: ANIME, isAdult: false) {
          title {
            userPreferred
          }
          id
          description
          coverImage {
            large
          }
          startDate {
            year
            month
            day
          }
          season
          popularity
          averageScore
          episodes
          genres
        }
      }
    }
  `;
        let variables = {
            title: title
        };

        var url = 'https://graphql.anilist.co',
            options = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                },
                body: JSON.stringify({
                    query: query,
                    variables: variables
                })
            };

        try {
            const response = await fetch(url, options);
            const data = await handleResponse(response);
            console.log(data.data);
            await this.uploadAnimeToDatabase(data, errorCallback);
            return data.data.Page.media;
        } catch (error) {
            handleError(error);
        }
        function handleResponse(response) {
            return response.json().then(function (json) {
                return response.ok ? json : Promise.reject(json);
            });
        }
        function handleError(error) {
            alert('Error, check console');
            console.error(error);
        }

    }

    async getAnimeByGenre(genre, errorCallback) {
        const map = new Map();
        let query = `
            query findAnimeByGenre($genre : String) {
                Page(page : 1, perPage : 50){
                    media(genre: $genre, type: ANIME){
                        title{
                        userPreferred
                        }
                        id
                description
                coverImage{
                    large
                }
                startDate {
                    year
                    month
                    day
                }
                season
                popularity
                averageScore
                episodes
                genres
                
                    }
                }
            }
        `;

        let variables = {
            genre: genre
        };

        var url = 'https://graphql.anilist.co',
            options = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json',
                },
                body: JSON.stringify({
                    query: query,
                    variables: variables
                })
            };

        try {
            const response = await fetch(url, options);
            const data = await handleResponse(response);
            console.log(data.data);
            await this.uploadAnimeToDatabase(data, errorCallback);
            return data.data.Page.media;
        } catch (error) {
            handleError(error);
        }

        function handleResponse(response) {
            return response.json().then(function (json) {
                return response.ok ? json : Promise.reject(json);
            });
        }
        function handleError(error) {
            alert('Error, check console');
            console.error(error);
        }

    }

    async uploadAnimeToDatabase(array, errorCallback) {
        console.log(array)
        try {
            const response = await this.client.post(`/anime/postSearch`, {
                graphQLResponse: array
            });
        } catch (error) {
            this.handleError("uploadAnimeToDatabase", error, errorCallback);
        }
    }

    async postReview(review, ID, userID, rating) {
        try {
            const response = await this.client.post('review', {
                review: review,
                animeID: ID,
                userID: userID,
                rating: rating
            });

            return response.data;
        } catch {
            this.handleError(("postReview", error, errorCallback));
        }
    }

    async getReviewsByAnimeID(ID, lastKey, errorCallback) {
        try {
            if (lastKey !== null) {
                console.log(lastKey)
                const response = await this.client.post(`review/limit`, {
                    animeID: ID,
                    valuesForReviews: {
                        animeID: lastKey.animeID,
                        postDate: lastKey.postDate,
                        reviewID: lastKey.reviewID
                    }
                });
                return response.data;
            } else {
                const response = await this.client.post(`review/limit`, {
                    animeID: ID,
                    valuesForReviews: {
                        animeID: 0,
                        postDate: 0,
                        reviewID: "string"
                    }
                });
                return response.data;
            }

        } catch (error) {
            this.handleError("uploadAnimeToDatabase", error, errorCallback);
        }

    }

    async getRatingForAnime(animeID, errorCallback){
        try {
            const response = await this.client.get(`review/${animeID}`)
            return response.data;
        }catch(error){
            this.handleError("getRatingForAnime", error, errorCallback)
        }

    }

    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }

    async getUserData(displayName, errorCallback) {
        try {
            const response = await this.client.get(`/user/${displayName}/searchByDisplayName/`);
            return response.data;
        }catch (error){
            this.handleError('getUserData', error, errorCallback)
        }
    }

    async addToFavorites(displayName, animeId, errorCallback) {
        try {
            const response = await this.client.post(`user/${displayName}/addFavorite/${animeId}`, {
                displayName: displayName,
                animeId: animeId
            });
            return response.data;
        } catch (error) {
            this.handleError('addToFavorites', error, errorCallback);
        }
    }

    async getNotifications (displayName, errorCallback) {
        try {
            const response = await this.client.get(`notification/getNotification/${displayName}`)
            return response.data;
        } catch (error) {
            this.handleError('getNotifications', error, errorCallback)
        }
    }


    async setNotification (displayName, notificationRequest, requester, errorCallback) {
        try {
            const response = await this.client.post(`/notification/setNotification/${displayName}`, {
                requestedUUID: displayName,
                userRequest: {
                    displayName: requester,
                    action: notificationRequest
                },
                hasBeenViewed:  false});
            return response.data;
        } catch (error) {
            this.handleError('setNotifications', error, errorCallback);
        }
    }

}
/**
 * Once the page has loaded, set up the event handlers and fetch the concert list.
 */