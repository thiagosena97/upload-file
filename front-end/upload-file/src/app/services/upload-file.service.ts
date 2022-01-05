import { HttpClient, HttpEvent, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UploadFileService {

  private baseUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  upload(file: File): Observable<HttpEvent<any>> {

    const formData: FormData = new FormData();

    formData.append('file', file);

    const requisicao = new HttpRequest('POST', `${this.baseUrl}/upload`, formData, {

      reportProgress: true,
      responseType: 'json'

    });

    return this.http.request(requisicao);

  }

  getFiles(): Observable<any> {

    return this.http.get(`${this.baseUrl}/files`);

  }
}