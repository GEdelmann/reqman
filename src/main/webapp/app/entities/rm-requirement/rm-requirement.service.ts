import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRMRequirement } from 'app/shared/model/rm-requirement.model';

type EntityResponseType = HttpResponse<IRMRequirement>;
type EntityArrayResponseType = HttpResponse<IRMRequirement[]>;

@Injectable({ providedIn: 'root' })
export class RMRequirementService {
    private resourceUrl = SERVER_API_URL + 'api/rm-requirements';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/rm-requirements';

    constructor(private http: HttpClient) {}

    create(rMRequirement: IRMRequirement): Observable<EntityResponseType> {
        return this.http.post<IRMRequirement>(this.resourceUrl, rMRequirement, { observe: 'response' });
    }

    update(rMRequirement: IRMRequirement): Observable<EntityResponseType> {
        return this.http.put<IRMRequirement>(this.resourceUrl, rMRequirement, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IRMRequirement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRMRequirement[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRMRequirement[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
