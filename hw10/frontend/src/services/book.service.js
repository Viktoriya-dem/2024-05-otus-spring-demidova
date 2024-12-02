import httpService from "./http.service";

const thingEndpoint = "books/";

const bookService = {
	fetch: async () => {
		const { data } = await httpService.get();
		console.log(data);
		return data
	}
}

export default bookService;