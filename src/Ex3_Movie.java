import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;


public class Ex3_Movie {

	static Ex3_Movie ex3 = new Ex3_Movie();

	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {

		//		File[] files = new File(".").listFiles(); //"." means root of project -> "AD Project 2015"

		long t1 = System.currentTimeMillis();
		Ex3_Movie.Rating[] ratings = getRatings(readData("ratings.dat"));
		Ex3_Movie.Movie[] movies = getMovies(readData("movies.dat"));
		Ex3_Movie.User[] users = getUsers(readData("users.dat"));
		long t2 = System.currentTimeMillis();
		System.out.println("\nTime to read data: " + (t2 - t1) + " ms\n");
		//		saveData(movies, "moviesMac.dat");

		HashMap<Integer, Movie> moviesHMap = getMovieHMAp(readData("movies.dat"));

		System.out.println("Size of HMap: " + moviesHMap.size());

		System.out.println("Size of ratings: " + ratings.length); //**
		System.out.println("Size of movies: " + movies.length); //**
		System.out.println("Size of users: " + users.length); //**

		t1 = System.currentTimeMillis();
		Ex3_Movie.Movie[] moviesTop = findTopAvgRate(movies, ratings, 25);
		t2 = System.currentTimeMillis();
		System.out.println("\nTime to search: " + (t2 - t1) + " ms\n");

		for (int i = moviesTop.length - 1; i >= 0; i--)
			System.out.println("Title: " + moviesTop[i].title + "   Avg. rate: " + moviesTop[i].avgRate);

		System.out.println();

		t1 = System.currentTimeMillis();
		Ex3_Movie.Movie[] moviesTop2 = findTopAvgRate2(movies, ratings, 25);
		t2 = System.currentTimeMillis();
		System.out.println("\nTime to search: " + (t2 - t1) + " ms\n");

		System.out.println();

		for (int i = moviesTop2.length - 1; i >= 0; i--)
			System.out.println("Title: " + moviesTop2[i].title + "   Avg. rate: " + moviesTop2[i].avgRate);

		t1 = System.currentTimeMillis();
		Ex3_Movie.Movie[] moviesTop3 = findTopAvgRate3(movies, ratings, 25);
		t2 = System.currentTimeMillis();
		System.out.println("\nTime to search: " + (t2 - t1) + " ms\n");

		System.out.println();

		for (int i = moviesTop3.length - 1; i >= 0; i--)
			System.out.println("Title: " + moviesTop3[i].title + "   Avg. rate: " + moviesTop3[i].avgRate);


		t1 = System.currentTimeMillis();
		Ex3_Movie.Movie[] moviesTop4 = findTopAvgRate4(movies, ratings, 25);
		t2 = System.currentTimeMillis();
		System.out.println("\nTime to search: " + (t2 - t1) + " ms\n");

		for (int i = moviesTop4.length - 1; i >= 0; i--)
			System.out.println("Title: " + moviesTop4[i].title + "   Avg. rate: " + moviesTop4[i].avgRate);



		t1 = System.currentTimeMillis();
		Ex3_Movie.Movie[] moviesTop6 = findTopAvgRate6(movies, ratings, 25);
		t2 = System.currentTimeMillis();
		System.out.println("\nTime to search: " + (t2 - t1) + " ms\n");

		for (int i = moviesTop6.length - 1; i >= 0; i--)
			System.out.println("Title: " + moviesTop6[i].title + "   Avg. rate: " + moviesTop6[i].avgRate);



		//		for(int i=0; i < ratings.length; i++) {
		//			Movie m = moviesHMap.get(ratings[i].movieId);
		//			m.addRating(ratings[i].rating);
		//		}



		//		System.out.println(moviesHMap.get(10).title);

		//		Movie m = moviesHMap.get(10);


		//int nRetMovies = Math.max(10, avgRate.length);
		//Ex3_Movie.Movie[] topMovies = new Ex3_Movie.Movie[nRetMovies];
	}

	public static Ex3_Movie.Movie[] findTopAvgRate(Ex3_Movie.Movie[] movies, Ex3_Movie.Rating[] ratings, int nRetMovie) {

		int l = movies[movies.length - 1].movieId + 1;

		int[] sumRatings = new int[l];
		int[] numRatings = new int[l];
		double[] avgRate = new double[l];

		int[] iMovie = new int[l];
		for(int i = 0; i < movies.length; i++)
			iMovie[movies[i].movieId] = i;

		for(Rating r : ratings) {
			int id = r.movieId;
			sumRatings[id] += r.rating;
			numRatings[id]++;
		}

		for(int id = 0; id < avgRate.length; id++) {
			Movie m = movies[iMovie[id]];
			if ((m.nRating = numRatings[id]) > 0) m.avgRate = avgRate[id] = (double) sumRatings[id] / numRatings[id];
		}

		nRetMovie = Math.min(nRetMovie, avgRate.length);

		Ex3_Movie.Movie[] topMovies = new Ex3_Movie.Movie[nRetMovie];
		double[] topRates = new double[nRetMovie];

		double minRateTop = Double.NEGATIVE_INFINITY;
		int nMinRateTop = 0;
		for (int i = 1; i < avgRate.length; i++) {
			double rate = avgRate[i];
			if (rate > minRateTop || nMinRateTop < nRetMovie - 1) {
				int pos = Arrays.binarySearch(topRates, nRetMovie - nMinRateTop, nRetMovie, rate);
				if (pos < 0) pos = -pos - 2;
				for (int j = nRetMovie - nMinRateTop - 1; j < pos; j++) {
					topMovies[j] = topMovies[j+1];
					topRates[j] = topRates[j+1];
				}
				topMovies[pos] = movies[iMovie[i]];
				topRates[pos] = rate;

				minRateTop = topRates[0];

				if (nMinRateTop < nRetMovie - 1) nMinRateTop++;
			}
		}
		return topMovies;
	}

	public static Ex3_Movie.Movie[] findTopAvgRate2(Ex3_Movie.Movie[] movies, Ex3_Movie.Rating[] ratings, int nRetMovie) {

		for(Movie m : movies)
			m.nRating = m.sumRating = 0;

		int[] iDMovies = new int[movies[movies.length - 1].movieId + 1];  //Should be changed to a HashMap

		for(int i = 0; i < movies.length; i++)
			iDMovies[movies[i].movieId] = i;

		for(Rating r : ratings) {
			Movie m = movies[iDMovies[r.movieId]];
			m.sumRating += r.rating;
			m.nRating++;
		}

		for(Movie m : movies)
			if (m.nRating > 0) m.avgRate = (double) m.sumRating / m.nRating;

		if (movies.length < nRetMovie) nRetMovie = movies.length;

		Movie[] topMovies = new Movie[nRetMovie];

		double minRateTop = Double.NEGATIVE_INFINITY;
		int nMinRateTop = 0;
		for (Movie m : movies) {
			if (m.avgRate > minRateTop || nMinRateTop < nRetMovie - 1) {
				int lo = nRetMovie - nMinRateTop;
				int hi = nRetMovie - 1;
				int mid;

				double avgRate = m.avgRate;
				if (lo > hi) mid = hi;
				else if (lo == hi) {
					if (topMovies[lo].avgRate > avgRate) mid = lo - 1; else mid = lo;
				}
				else 
					while (true) {
						if (lo >= hi - 1) {
							if (topMovies[lo].avgRate > avgRate) mid = lo - 1;
							else if (topMovies[hi].avgRate < avgRate) mid = hi;
							else mid = lo;
							break;
						}
						mid = (hi + lo) / 2;
						double rateMid = topMovies[mid].avgRate;
						if (rateMid > avgRate) hi = mid - 1;
						else if (rateMid < avgRate) lo = mid + 1;
						else break;
					}

				for (int j = nRetMovie - nMinRateTop - 1; j < mid; j++) 
					topMovies[j] = topMovies[j+1];

				topMovies[mid] = m;

				if (nMinRateTop < nRetMovie - 1) nMinRateTop++;
				else minRateTop = topMovies[0].avgRate;
			}
		}
		return topMovies;
	}

	public static Ex3_Movie.Movie[] findTopAvgRate3(Ex3_Movie.Movie[] movies, Ex3_Movie.Rating[] ratings, int nRetMovie) {

		for(Movie m : movies)
			m.nRating = m.sumRating = 0;

		HashMap<Integer, Integer> hMapIdMovies = new HashMap<>();
		for(int i = 0; i < movies.length; i++)
			hMapIdMovies.put(movies[i].movieId, i);

		for(Rating r : ratings) {
			Movie m = movies[hMapIdMovies.get(r.movieId)];
			m.sumRating += r.rating;
			m.nRating++;
		}

		for(Movie m : movies)
			if (m.nRating > 0) m.avgRate = (double) m.sumRating / m.nRating;

		if (movies.length < nRetMovie) nRetMovie = movies.length;

		Movie[] topMovies = new Movie[nRetMovie];

		//		Comparator<Movie> comparatorMovieAvgRate = ex3.new MovieRateComparator();
		//		PriorityQueue<Movie> pq = new PriorityQueue<Movie>(nRetMovie, comparatorMovieAvgRate);

		double minRateTop = Double.NEGATIVE_INFINITY;
		int nMinRateTop = 0;
		for (Movie m : movies) {
			if (m.avgRate > minRateTop || nMinRateTop < nRetMovie - 1) {
				int lo = nRetMovie - nMinRateTop;
				int hi = nRetMovie - 1;
				int mid;

				double avgRate = m.avgRate;
				if (lo > hi) mid = hi;
				else if (lo == hi) {
					if (topMovies[lo].avgRate > avgRate) mid = lo - 1; else mid = lo;
				}
				else 
					while (true) {
						if (lo >= hi - 1) {
							if (topMovies[lo].avgRate > avgRate) mid = lo - 1;
							else if (topMovies[hi].avgRate < avgRate) mid = hi;
							else mid = lo;
							break;
						}
						mid = (hi + lo) / 2;
						double rateMid = topMovies[mid].avgRate;
						if (rateMid > avgRate) hi = mid - 1;
						else if (rateMid < avgRate) lo = mid + 1;
						else break;
					}

				for (int j = nRetMovie - nMinRateTop - 1; j < mid; j++) 
					topMovies[j] = topMovies[j+1];

				topMovies[mid] = m;

				if (nMinRateTop < nRetMovie - 1) nMinRateTop++;
				else minRateTop = topMovies[0].avgRate;
			}
		}
		return topMovies;
	}

	public static Ex3_Movie.Movie[] findTopAvgRate4(Ex3_Movie.Movie[] movies, Ex3_Movie.Rating[] ratings, int nRetMovie) {

		for(Movie m : movies)
			m.nRating = m.sumRating = 0;

		HashMap<Integer, Integer> hMapIdMovies = new HashMap<>();
		for(int i = 0; i < movies.length; i++)
			hMapIdMovies.put(movies[i].movieId, i);

		for(Rating r : ratings) {
			Movie m = movies[hMapIdMovies.get(r.movieId)];
			m.sumRating += r.rating;
			m.nRating++;
		}

		for(Movie m : movies)
			if (m.nRating > 0) m.avgRate = (double) m.sumRating / m.nRating;

		if (movies.length < nRetMovie) nRetMovie = movies.length;

		Comparator<Movie> comparatorMovieAvgRate = ex3.new MovieRateComparator();
		PriorityQueue<Movie> topMovies = new PriorityQueue<Movie>(nRetMovie, comparatorMovieAvgRate);

		int iMovie = 0;
		for (; iMovie < nRetMovie; iMovie++)
			topMovies.add(movies[iMovie]);

		double minRateInTop = topMovies.peek().avgRate;

		for (; iMovie < movies.length; iMovie++) {
			Movie m = movies[iMovie];
			if (m.avgRate > minRateInTop) {
				topMovies.remove();
				topMovies.add(m);
				minRateInTop = topMovies.peek().avgRate;
			}
		}

		//		while(!topMovies.isEmpty()) {
		//			Movie m = topMovies.remove();
		//			System.out.println("Title: " + m.title + "   Avg. rate: " + m.avgRate);
		//		}

		Movie[] topMoviesRet = new Movie[nRetMovie];
		for(int i = nRetMovie - 1; i >= 0; i--)
			topMoviesRet[i] = topMovies.remove();

		return topMoviesRet;
	}

	//	public static Ex3_Movie.Movie[] findTopAvgRate5(Ex3_Movie.Movie[] movies, Ex3_Movie.Rating[] ratings, int nRetMovie) {
	//
	//		if (movies.length < nRetMovie) nRetMovie = movies.length;
	//
	//		reset(movies);
	//		HashMap<Integer, Integer> hMapIdMovies = mapMovieId(movies);
	//		rating(movies, ratings, hMapIdMovies);
	//		avgRate(movies);
	//		Movie[] topMoviesRet = getTopMovies(movies, nRetMovie);
	//		
	//		return topMoviesRet;
	//	}
	//
	//	public static void reset(Movie[] movies) {
	//		for(Movie m : movies)
	//			m.nRating = m.sumRating = 0;
	//	}
	//
	//	public static HashMap<Integer, Integer> mapMovieId(Movie[] movies) {
	//		HashMap<Integer, Integer> hMapIdMovies = new HashMap<>();
	//		for(int i = 0; i < movies.length; i++)
	//			hMapIdMovies.put(movies[i].movieId, i);
	//		return hMapIdMovies;
	//	}
	//
	//	public static void rating(Movie[] movies, Rating[] ratings, HashMap<Integer, Integer> hMapIdMovies) {
	//		for(Rating r : ratings) {
	//			Movie m = movies[hMapIdMovies.get(r.movieId)];
	//			m.sumRating += r.rating;
	//			m.nRating++;
	//		}
	//	}
	//	public static void avgRate(Movie[] movies) {
	//		for(Movie m : movies)
	//			if (m.nRating > 0) m.avgRate = (double) m.sumRating / m.nRating;
	//	}
	//
	//
	//	public static Movie[] getTopMovies(Movie[] movies, int nRetMovie) {
	//		Comparator<Movie> comparatorMovieAvgRate = ex3.new MovieRateComparator();
	//		PriorityQueue<Movie> topMovies = new PriorityQueue<Movie>(nRetMovie, comparatorMovieAvgRate);
	//
	//		int iMovie = 0;
	//		for (; iMovie < nRetMovie; iMovie++)
	//			topMovies.add(movies[iMovie]);
	//
	//		double minRateInTop = topMovies.peek().avgRate;
	//
	//		for (; iMovie < movies.length; iMovie++) {
	//			Movie m = movies[iMovie];
	//			if (m.avgRate > minRateInTop) {
	//				topMovies.remove();
	//				topMovies.add(m);
	//				minRateInTop = topMovies.peek().avgRate;
	//			}
	//		}
	//
	//		Movie[] topMoviesRet = new Movie[nRetMovie];
	//		for(int i = nRetMovie - 1; i >= 0; i--)
	//			topMoviesRet[i] = topMovies.remove();
	//
	//		return topMoviesRet;
	//	}

	public static Ex3_Movie.Movie[] findTopAvgRate6(Ex3_Movie.Movie[] movies, Ex3_Movie.Rating[] ratings, int nRetMovie) {

		if (movies.length < nRetMovie) nRetMovie = movies.length;

		int nThread = 8;
		int[][] indexRanges = new int[nThread][2]; // 0:From index   1:To index

		for(int i = 0; i < nThread; i++)
			indexRanges[i][0] = movies.length / (nThread + 1) * i;
		for(int i = 0; i < nThread - 1; i++)
			indexRanges[i][1] = indexRanges[i + 1][0] - 1;
		indexRanges[nThread - 1][1] = movies.length - 1;


		for(int i = 0; i < nThread; i++)
			reset(movies);
		HashMap<Integer, Integer> hMapIdMovies = mapMovieId(movies);
		rating(movies, ratings, hMapIdMovies);
		avgRate(movies);
		Movie[] topMoviesRet = getTopMovies(movies, nRetMovie);

		//mergeTopMovie();

		return topMoviesRet;
	}

	public static void reset(Movie[] movies) {
		for(Movie m : movies)
			m.nRating = m.sumRating = 0;
	}

	public static HashMap<Integer, Integer> mapMovieId(Movie[] movies) {
		HashMap<Integer, Integer> hMapIdMovies = new HashMap<>();
		for(int i = 0; i < movies.length; i++)
			hMapIdMovies.put(movies[i].movieId, i);
		return hMapIdMovies;
	}

	public static void rating(Movie[] movies, Rating[] ratings, HashMap<Integer, Integer> hMapIdMovies) {
		for(Rating r : ratings) {
			Movie m = movies[hMapIdMovies.get(r.movieId)];
			m.sumRating += r.rating;
			m.nRating++;
		}
	}
	public static void avgRate(Movie[] movies) {
		for(Movie m : movies)
			if (m.nRating > 0) m.avgRate = (double) m.sumRating / m.nRating;
	}


	public static Movie[] getTopMovies(Movie[] movies, int nRetMovie) {
		Comparator<Movie> comparatorMovieAvgRate = ex3.new MovieRateComparator();
		PriorityQueue<Movie> topMovies = new PriorityQueue<Movie>(nRetMovie, comparatorMovieAvgRate);

		int iMovie = 0;
		for (; iMovie < nRetMovie; iMovie++)
			topMovies.add(movies[iMovie]);

		double minRateInTop = topMovies.peek().avgRate;

		for (; iMovie < movies.length; iMovie++) {
			Movie m = movies[iMovie];
			if (m.avgRate > minRateInTop) {
				topMovies.remove();
				topMovies.add(m);
				minRateInTop = topMovies.peek().avgRate;
			}
		}

		Movie[] topMoviesRet = new Movie[nRetMovie];
		for(int i = nRetMovie - 1; i >= 0; i--)
			topMoviesRet[i] = topMovies.remove();

		return topMoviesRet;
	}



	public class MovieRateComparator implements Comparator<Movie> {
		@Override
		public int compare(Movie m1, Movie m2) {
			if (m1.avgRate < m2.avgRate) return -1;
			if (m1.avgRate > m2.avgRate) return 1;
			return 0;
		}
	}

	public static ArrayList<String[]> readData(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
				if (!file.exists()) file = new File(".\\data\\ml-1m" + '\\' + fileName);

		if (file.exists()) System.out.println("Sti OK"); //**

		if (!file.exists()) file = new File("F:\\Dropbox\\Algorithm Design Project\\Exercises\\ex3\\ml-1m" + '\\' + fileName);
		if (!file.exists()) return new ArrayList<String[]>(); // ?? null

		Scanner scanner = new Scanner(file);

		ArrayList<String[]> data = new ArrayList<>();

		while (scanner.hasNextLine()) {
			String line = scanner.nextLine().trim();
			if (line.length() > 0 && line.charAt(0) != '#') data.add(line.split("::"));
		}
		scanner.close();
		return data;
	}


	public static HashMap<Integer, Movie> getMovieHMAp(ArrayList<String[]> data) {
		HashMap<Integer, Movie> moviesHMap = new HashMap<>(); 
		for(int i=0; i < data.size(); i++)
			moviesHMap.put(Integer.parseInt(data.get(i)[0]), ex3.new Movie(data.get(i)[0], data.get(i)[1], data.get(i)[2]));
		return moviesHMap;
	}

	public static Ex3_Movie.Rating[] getRatings(ArrayList<String[]> data) {
		Ex3_Movie.Rating[] ratings = new Ex3_Movie.Rating[data.size()];
		for(int i=0; i < data.size(); i++)
			ratings[i] = ex3.new Rating(data.get(i)[0], data.get(i)[1], data.get(i)[2], data.get(i)[3]);
		return ratings;
	}

	public static Ex3_Movie.Movie[] getMovies(ArrayList<String[]> data) {
		Ex3_Movie.Movie[] movies = new Ex3_Movie.Movie[data.size()];
		for(int i=0; i < data.size(); i++)
			movies[i] = ex3.new Movie(data.get(i)[0], data.get(i)[1], data.get(i)[2]);
		return movies;
	}

	public static Ex3_Movie.User[] getUsers(ArrayList<String[]> data) {
		Ex3_Movie.User[] users = new Ex3_Movie.User[data.size()];
		for(int i=0; i < data.size(); i++)
			users[i] = ex3.new User(data.get(i)[0], data.get(i)[1], data.get(i)[2], data.get(i)[3], data.get(i)[4]);
		return users;
	}

	public static void saveData(Movie[] movies, String fileName) throws FileNotFoundException, UnsupportedEncodingException {
		File file = new File("F:\\Dropbox\\Algorithm Design Project\\Exercises\\ex3\\ml-1m" + '\\' + fileName);
		PrintWriter writer = new PrintWriter(file, "UTF-8");


		for(int i=0; i < movies.length; i++) {
			writer.print(movies[i].movieId + "::" + movies[i].title + "::");

			for (int j=0; j < movies[i].genre.length - 1; j++)
				writer.print(movies[i].genre[j] + "___");

			if (movies[i].genre.length > 0) writer.print(movies[i].genre[movies[i].genre.length - 1]);
			writer.println();
		}
		writer.close();
	}



	class Rating {
		int user;
		int movieId;
		int rating;
		int timestamp;

		Rating(String user, String movieId, String rating, String timestamp) {
			this.user = Integer.parseInt(user);
			this.movieId = Integer.parseInt(movieId);
			this.rating = Integer.parseInt(rating);
			this.timestamp = Integer.parseInt(timestamp);
		}
	}

	class Movie implements Comparable<Movie>{
		int movieId;
		String title;
		String[] genre;

		int nRating = 0;
		int sumRating = 0;
		double avgRate = -1;

		Movie(String movieId, String title, String genre) {
			this.movieId = Integer.parseInt(movieId);
			this.title = title;
			this.genre = genre.split("\\|"); // !!
		}

		@Override
		public int compareTo(Movie m) {
			if (avgRate < m.avgRate) return -1;
			if (avgRate > m.avgRate) return 1;
			return 0;
		}
	}

	class User {
		int userId;
		char sex;
		int age;
		int occupation;
		String zipCode;
		User(String userId, String sex, String age, String occupation, String zipCode) {
			this.userId = Integer.parseInt(userId);
			this.sex = sex.charAt(0);
			this.age = Integer.parseInt(age);
			this.zipCode = zipCode;
		}
	}
}
