import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmployeeType } from '../employee-type.model';
import { EmployeeTypeService } from '../service/employee-type.service';

@Injectable({ providedIn: 'root' })
export class EmployeeTypeRoutingResolveService implements Resolve<IEmployeeType | null> {
  constructor(protected service: EmployeeTypeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeType | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((employeeType: HttpResponse<IEmployeeType>) => {
          if (employeeType.body) {
            return of(employeeType.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
