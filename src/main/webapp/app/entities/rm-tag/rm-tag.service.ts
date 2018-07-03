import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRMTag } from 'app/shared/model/rm-tag.model';

type EntityResponseType = HttpResponse<IRMTag>;
type EntityArrayResponseType = HttpResponse<IRMTag[]>;

@Injectable({ providedIn: 'root' })
export class RMTagService {
    private resourceUrl = SERVER_API_URL + 'api/rm-tags';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/rm-tags';

    constructor(private http: HttpClient) {}

    create(rMTag: IRMTag): Observable<EntityResponseType> {
        return this.http.post<IRMTag>(this.resourceUrl, rMTag, { observe: 'response' });
    }

    update(rMTag: IRMTag): Observable<EntityResponseType> {
        return this.http.put<IRMTag>(this.resourceUrl, rMTag, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IRMTag>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRMTag[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRMTag[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
