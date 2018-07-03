import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { RMTag } from 'app/shared/model/rm-tag.model';
import { RMTagService } from './rm-tag.service';
import { RMTagComponent } from './rm-tag.component';
import { RMTagDetailComponent } from './rm-tag-detail.component';
import { RMTagUpdateComponent } from './rm-tag-update.component';
import { RMTagDeletePopupComponent } from './rm-tag-delete-dialog.component';
import { IRMTag } from 'app/shared/model/rm-tag.model';

@Injectable({ providedIn: 'root' })
export class RMTagResolve implements Resolve<IRMTag> {
    constructor(private service: RMTagService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((rMTag: HttpResponse<RMTag>) => rMTag.body);
        }
        return Observable.of(new RMTag());
    }
}

export const rMTagRoute: Routes = [
    {
        path: 'rm-tag',
        component: RMTagComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMTag.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-tag/:id/view',
        component: RMTagDetailComponent,
        resolve: {
            rMTag: RMTagResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMTag.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-tag/new',
        component: RMTagUpdateComponent,
        resolve: {
            rMTag: RMTagResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMTag.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-tag/:id/edit',
        component: RMTagUpdateComponent,
        resolve: {
            rMTag: RMTagResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMTag.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const rMTagPopupRoute: Routes = [
    {
        path: 'rm-tag/:id/delete',
        component: RMTagDeletePopupComponent,
        resolve: {
            rMTag: RMTagResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMTag.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
