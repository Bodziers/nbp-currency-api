import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Rate} from "../common/rate";

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private httpClient: HttpClient) {  }

  private baseUrl = 'http://localhost:8080/api'

  getRatesList(date : string) : Observable<Rate[]> {
    return this.httpClient.get<Rate[]>(`${this.baseUrl}/table/${date}`)
  }

  saveRatesToFile(date : string) : Observable<Rate[]> {
    return this.httpClient.get<Rate[]>(`${this.baseUrl}/save/${date}`)
  }

  calculateExRate(date : string,
                  fromCurrencyCode : string,
                  toCurrencyCode : string,
                  amount : number) : Observable<Rate[]> {
    return this.httpClient.get<Rate[]>(`${this.baseUrl}/calculate/${date}/${fromCurrencyCode}/${toCurrencyCode}/${amount}`)
  }
}
