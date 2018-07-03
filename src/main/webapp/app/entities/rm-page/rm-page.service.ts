import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRMPage } from 'app/shared/model/rm-page.model';

type EntityResponseType = HttpResponse<IRMPage>;
type EntityArrayResponseType = HttpResponse<IRMPage[]>;

@Injectable({ providedIn: 'root' })
export class RMPageService {
    private resourceUrl = SERVER_API_URL + 'api/rm-pages';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/rm-pages';

    constructor(private http: HttpClient) {}

    create(rMPage: IRMPage): Observable<EntityResponseType> {
        return this.http.post<IRMPage>(this.resourceUrl, rMPage, { observe: 'response' });
    }

    update(rMPage: IRMPage): Observable<EntityResponseType> {
        return this.http.put<IRMPage>(this.resourceUrl, rMPage, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IRMPage>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRMPage[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRMPage[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
