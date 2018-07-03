/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReqmanTestModule } from '../../../test.module';
import { RMRequirementDetailComponent } from 'app/entities/rm-requirement/rm-requirement-detail.component';
import { RMRequirement } from 'app/shared/model/rm-requirement.model';

describe('Component Tests', () => {
    describe('RMRequirement Management Detail Component', () => {
        let comp: RMRequirementDetailComponent;
        let fixture: ComponentFixture<RMRequirementDetailComponent>;
        const route = ({ data: of({ rMRequirement: new RMRequirement(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [ReqmanTestModule],
                declarations: [RMRequirementDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RMRequirementDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RMRequirementDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.rMRequirement).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
