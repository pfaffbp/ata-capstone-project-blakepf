import BaseClass from "../util/baseClass";
import DataStore from "../util/DataStore";
import ExampleClient from "../api/exampleClient";
import axios from "axios";

<<<<<<< HEAD
<<<<<<< HEAD
=======

>>>>>>> origin/new-branch-for-dev
=======

>>>>>>> c6fefe0bdc68b0e95f169879a93e8edffda79bc0
/**
 * Logic needed for the view playlist page of the website.
 */
export default class AnimeClient extends BaseClass {

    constructor(props = {}) {
        super();
<<<<<<< HEAD
<<<<<<< HEAD
        const methodsToBind = ['getAnimeInfo'];
=======
        const methodsToBind = ['getAnimeInfo','getAnimeBySearch'];
>>>>>>> origin/new-branch-for-dev
=======
        const methodsToBind = ['getAnimeInfo','getAnimeBySearch'];
>>>>>>> c6fefe0bdc68b0e95f169879a93e8edffda79bc0
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

    async getAnimeInfo(id, errorCallback){
        try{
            const response = await this.client.get(`/anime/${id}`)
            return response.data;
        }catch(error){
<<<<<<< HEAD
<<<<<<< HEAD
            this.handleError("getExample", error, errorCallback)
        }
    }

    async getAnimeBySearch(title, errorCallback){
        let query = `
        query findPopularAnime($title: String){
    Page(page : 1, perPage : 50){
        media(search: $title type : ANIME){
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
            search : title
=======
=======
>>>>>>> c6fefe0bdc68b0e95f169879a93e8edffda79bc0

        }
    }

    async getAnimeBySearch(title, errorCallback) {
        const map = new Map();
        let query = `
    query findPopularAnime($title: String) {
      Page(page: 1, perPage: 50) {
        media(search: $title, type: ANIME) {
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
<<<<<<< HEAD
>>>>>>> origin/new-branch-for-dev
=======
>>>>>>> c6fefe0bdc68b0e95f169879a93e8edffda79bc0
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

<<<<<<< HEAD
<<<<<<< HEAD
        fetch(url, options).then(handleResponse)
            .then(handleData)
            .catch(handleError);

=======
=======
>>>>>>> c6fefe0bdc68b0e95f169879a93e8edffda79bc0
        try {
            const response = await fetch(url, options);
            const data = await handleResponse(response);
            return data.data.Page.media;
        } catch (error) {
            handleError(error);
        }
<<<<<<< HEAD
>>>>>>> origin/new-branch-for-dev
=======
>>>>>>> c6fefe0bdc68b0e95f169879a93e8edffda79bc0
        function handleResponse(response) {
            return response.json().then(function (json) {
                return response.ok ? json : Promise.reject(json);
            });
        }
<<<<<<< HEAD
<<<<<<< HEAD

        function handleData(data) {
            console.log(data);
        }

=======
>>>>>>> origin/new-branch-for-dev
=======
>>>>>>> c6fefe0bdc68b0e95f169879a93e8edffda79bc0
        function handleError(error) {
            alert('Error, check console');
            console.error(error);
        }

    }

<<<<<<< HEAD
<<<<<<< HEAD
    handleError(method, error, errorCallback) {
        console.error(method + " failed - " + error);
        if (error.response.data.message !== undefined) {
            console.error(error.response.data.message);
        }
        if (errorCallback) {
            errorCallback(method + " failed - " + error);
        }
    }
=======
>>>>>>> origin/new-branch-for-dev
=======
>>>>>>> c6fefe0bdc68b0e95f169879a93e8edffda79bc0
}
    /**
     * Once the page has loaded, set up the event handlers and fetch the concert list.
     */
