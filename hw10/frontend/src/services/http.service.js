import axios from "axios";


axios.defaults.baseURL = "/api/books";

const httpService = {
	get: axios.get,
};

export default httpService;
