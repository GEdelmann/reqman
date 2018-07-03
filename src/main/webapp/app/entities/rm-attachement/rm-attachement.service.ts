import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRMAttachement } from 'app/shared/model/rm-attachement.model';

type EntityResponseType = HttpResponse<IRMAttachement>;
type EntityArrayResponseType = HttpResponse<IRMAttachement[]>;

@Injectable({ providedIn: 'root' })
export class RMAttachementService {
    private resourceUrl = SERVER_API_URL + 'api/rm-attachements';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/rm-attachements';

    constructor(private http: HttpClient) {}

    create(rMAttachement: IRMAttachement): Observable<EntityResponseType> {
        return this.http.post<IRMAttachement>(this.resourceUrl, rMAttachement, { observe: 'response' });
    }

    update(rMAttachement: IRMAttachement): Observable<EntityResponseType> {
        return this.http.put<IRMAttachement>(this.resourceUrl, rMAttachement, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IRMAttachement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRMAttachement[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRMAttachement[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
