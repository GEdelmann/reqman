import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRMCategory } from 'app/shared/model/rm-category.model';

type EntityResponseType = HttpResponse<IRMCategory>;
type EntityArrayResponseType = HttpResponse<IRMCategory[]>;

@Injectable({ providedIn: 'root' })
export class RMCategoryService {
    private resourceUrl = SERVER_API_URL + 'api/rm-categories';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/rm-categories';

    constructor(private http: HttpClient) {}

    create(rMCategory: IRMCategory): Observable<EntityResponseType> {
        return this.http.post<IRMCategory>(this.resourceUrl, rMCategory, { observe: 'response' });
    }

    update(rMCategory: IRMCategory): Observable<EntityResponseType> {
        return this.http.put<IRMCategory>(this.resourceUrl, rMCategory, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IRMCategory>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRMCategory[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRMCategory[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
