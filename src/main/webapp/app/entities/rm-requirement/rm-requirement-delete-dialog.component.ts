import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRMRequirement } from 'app/shared/model/rm-requirement.model';
import { RMRequirementService } from './rm-requirement.service';

@Component({
    selector: '-rm-requirement-delete-dialog',
    templateUrl: './rm-requirement-delete-dialog.component.html'
})
export class RMRequirementDeleteDialogComponent {
    rMRequirement: IRMRequirement;

    constructor(
        private rMRequirementService: RMRequirementService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.rMRequirementService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'rMRequirementListModification',
                content: 'Deleted an rMRequirement'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: '-rm-requirement-delete-popup',
    template: ''
})
export class RMRequirementDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rMRequirement }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RMRequirementDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.rMRequirement = rMRequirement;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
