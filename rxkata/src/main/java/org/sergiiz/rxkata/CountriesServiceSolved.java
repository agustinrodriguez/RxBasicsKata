package org.sergiiz.rxkata;

import java.util.List;
import java.util.Map;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Single;

class CountriesServiceSolved implements CountriesService {

	@Override
	public Single<String> countryNameInCapitals(Country country) {
		return Single.just(country).map(Country::getName).map(String::toUpperCase);
	}

	public Single<Integer> countCountries(List<Country> countries) {
		return Single.just(countries).map(List::size);
	}

	public Observable<Long> listPopulationOfEachCountry(List<Country> countries) {
		return Observable.fromIterable(countries).map(Country::getPopulation);
	}

	@Override
	public Observable<String> listNameOfEachCountry(List<Country> countries) {
		return Observable.fromIterable(countries).map(Country::getName);
	}

	@Override
	public Observable<Country> listOnly3rdAnd4thCountry(List<Country> countries) {
		return Observable.fromIterable(countries).skip(2).take(2);
	}

	@Override
	public Single<Boolean> isAllCountriesPopulationMoreThanOneMillion(List<Country> countries) {
		return Observable.fromIterable(countries).all(Country::hasMoreThanOneMillionPopulation);
	}

	@Override
	public Observable<Country> listPopulationMoreThanOneMillion(List<Country> countries) {
		return Observable.fromIterable(countries).filter(Country::hasMoreThanOneMillionPopulation);
	}

	@Override
	public Observable<Country> listPopulationMoreThanOneMillionWithTimeoutFallbackToEmpty(final FutureTask<List<Country>> countriesFromNetwork) {
		return Observable.fromFuture(countriesFromNetwork, 1000, TimeUnit.MILLISECONDS).onErrorResumeNext(Observable.empty())
				.flatMapIterable(countries -> countries).filter(Country::hasMoreThanOneMillionPopulation);
	}

	@Override
	public Observable<String> getCurrencyUsdIfNotFound(String countryName, List<Country> countries) {
		return Observable.fromIterable(countries).filter(country -> country.getName().equals(countryName)).map(Country::getCurrency)
				.defaultIfEmpty("USD");
	}

	@Override
	public Observable<Long> sumPopulationOfCountries(List<Country> countries) {
		return Observable.fromIterable(countries).reduce(0L, (sum, country) -> sum + country.getPopulation()).toObservable();
	}

	@Override
	public Single<Map<String, Long>> mapCountriesToNamePopulation(List<Country> countries) {
		return Observable.fromIterable(countries).toMap(country -> country.getName(), country -> country.getPopulation());
	}

	@Override
	public Observable<Long> sumPopulationOfCountries(Observable<Country> countryObservable1, Observable<Country> countryObservable2) {
		return countryObservable1.mergeWith(countryObservable2).reduce(0L, (sum, country) -> sum + country.getPopulation()).toObservable();
	}

	@Override
	public Single<Boolean> areEmittingSameSequences(Observable<Country> countryObservable1, Observable<Country> countryObservable2) {
		return Observable.sequenceEqual(countryObservable1, countryObservable2);
	}
}
