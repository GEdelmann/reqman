import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable } from 'rxjs';
import { RMRequirement } from 'app/shared/model/rm-requirement.model';
import { RMRequirementService } from './rm-requirement.service';
import { RMRequirementComponent } from './rm-requirement.component';
import { RMRequirementDetailComponent } from './rm-requirement-detail.component';
import { RMRequirementUpdateComponent } from './rm-requirement-update.component';
import { RMRequirementDeletePopupComponent } from './rm-requirement-delete-dialog.component';
import { IRMRequirement } from 'app/shared/model/rm-requirement.model';

@Injectable({ providedIn: 'root' })
export class RMRequirementResolve implements Resolve<IRMRequirement> {
    constructor(private service: RMRequirementService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).map((rMRequirement: HttpResponse<RMRequirement>) => rMRequirement.body);
        }
        return Observable.of(new RMRequirement());
    }
}

export const rMRequirementRoute: Routes = [
    {
        path: 'rm-requirement',
        component: RMRequirementComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMRequirement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-requirement/:id/view',
        component: RMRequirementDetailComponent,
        resolve: {
            rMRequirement: RMRequirementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMRequirement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-requirement/new',
        component: RMRequirementUpdateComponent,
        resolve: {
            rMRequirement: RMRequirementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMRequirement.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'rm-requirement/:id/edit',
        component: RMRequirementUpdateComponent,
        resolve: {
            rMRequirement: RMRequirementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMRequirement.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const rMRequirementPopupRoute: Routes = [
    {
        path: 'rm-requirement/:id/delete',
        component: RMRequirementDeletePopupComponent,
        resolve: {
            rMRequirement: RMRequirementResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'reqmanApp.rMRequirement.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
