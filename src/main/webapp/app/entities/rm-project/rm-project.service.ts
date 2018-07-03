import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRMProject } from 'app/shared/model/rm-project.model';

type EntityResponseType = HttpResponse<IRMProject>;
type EntityArrayResponseType = HttpResponse<IRMProject[]>;

@Injectable({ providedIn: 'root' })
export class RMProjectService {
    private resourceUrl = SERVER_API_URL + 'api/rm-projects';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/rm-projects';

    constructor(private http: HttpClient) {}

    create(rMProject: IRMProject): Observable<EntityResponseType> {
        return this.http.post<IRMProject>(this.resourceUrl, rMProject, { observe: 'response' });
    }

    update(rMProject: IRMProject): Observable<EntityResponseType> {
        return this.http.put<IRMProject>(this.resourceUrl, rMProject, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IRMProject>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRMProject[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRMProject[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
