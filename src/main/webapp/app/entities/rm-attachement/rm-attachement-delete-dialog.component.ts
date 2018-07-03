import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRMAttachement } from 'app/shared/model/rm-attachement.model';
import { RMAttachementService } from './rm-attachement.service';

@Component({
    selector: '-rm-attachement-delete-dialog',
    templateUrl: './rm-attachement-delete-dialog.component.html'
})
export class RMAttachementDeleteDialogComponent {
    rMAttachement: IRMAttachement;

    constructor(
        private rMAttachementService: RMAttachementService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.rMAttachementService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'rMAttachementListModification',
                content: 'Deleted an rMAttachement'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: '-rm-attachement-delete-popup',
    template: ''
})
export class RMAttachementDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ rMAttachement }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RMAttachementDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.rMAttachement = rMAttachement;
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
