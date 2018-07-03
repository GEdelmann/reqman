import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRMProject } from 'app/shared/model/rm-project.model';
import { RMProjectService } from './rm-project.service';

@Component({
    selector: '-rm-project-delete-dialog',
    templateUrl: './rm-project-delete-dialog.component.html'
})
export class RMProjectDeleteDialogComponent {
    rMProject: IRMProject;

    constructor(private rMProjectService: RMProjectService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.rMProjectService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'rMProjectListModification',
                content: 'Deleted an rMProject'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: '-rm-project-delete-popup',
    template: ''
})
export class RMProjectDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rMProject }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RMProjectDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.rMProject = rMProject;
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
