import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { RMAttachement } from 'app/shared/model/rm-attachement.model';
import { RMAttachementService } from './rm-attachement.service';
import { RMAttachementComponent } from './rm-attachement.component';
import { RMAttachementDetailComponent } from './rm-attachement-detail.component';
import { RMAttachementUpdateComponent } from './rm-attachement-update.component';
import { RMAttachementDeletePopupComponent } from './rm-attachement-delete-dialog.component';
import { IRMAttachement } from 'app/shared/model/rm-attachement.model';

@Injectable({ providedIn: 'root' })
export class RMAttachementResolve implements Resolve<IRMAttachement> {
    constructor(private service: RMAttachementService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((rMAttachement: HttpResponse<RMAttachement>) => rMAttachement.body);
        }
        return Observable.of(new RMAttachement());
    }
}

export const rMAttachementRoute: Routes = [
    {
        path: 'rm-attachement',
        component: RMAttachementComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'reqmanApp.rMAttachement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-attachement/:id/view',
        component: RMAttachementDetailComponent,
        resolve: {
            rMAttachement: RMAttachementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMAttachement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-attachement/new',
        component: RMAttachementUpdateComponent,
        resolve: {
            rMAttachement: RMAttachementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMAttachement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-attachement/:id/edit',
        component: RMAttachementUpdateComponent,
        resolve: {
            rMAttachement: RMAttachementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMAttachement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const rMAttachementPopupRoute: Routes = [
    {
        path: 'rm-attachement/:id/delete',
        component: RMAttachementDeletePopupComponent,
        resolve: {
            rMAttachement: RMAttachementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMAttachement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
