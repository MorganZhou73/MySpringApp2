import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Inject, Injectable } from '@angular/core';
import { catchError, Observable, of, throwError, timeout } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

	constructor(@Inject(HttpClient) private http: HttpClient) {
	}
	
	httpGet(url: string, param?: any): Observable<any> {
		return this.http.get(environment.baseUrl + url, { ...param }).pipe(timeout(5000));
		// .pipe(tap((responseData) => {console.log(responseData); }), 
		// catchError(this.handleError));
	}
	
	httpPost(url: string, body?: any, param?: any): Observable<any> {
		return this.http.post(environment.baseUrl + url, body, { ...param }).pipe(timeout(5000), catchError(this.handleError));
	}
	
	httpPut(url: string, body?: any, param?: any): Observable<any> {
		return this.http.put(environment.baseUrl + url, body, { ...param }).pipe(timeout(5000), catchError(this.handleError));
	}
	
	httpDelete(url: string, param?: any): Observable<any> {
		return this.http.delete(environment.baseUrl + url, { ...param }).pipe(timeout(5000), catchError(this.handleError));
	}
	
	private handleError(error: HttpErrorResponse) {
		console.log('...error :', error);
		if(error.status == 302) {
			console.log('...returning error.error : ', error.error);
			return of({status: 200, body: error.error });
		}
		
		return throwError(() => error);
	}
}
