import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEmployeeAccount } from '../employee-account.model';
import { EmployeeAccountService } from '../service/employee-account.service';

@Injectable({ providedIn: 'root' })
export class EmployeeAccountRoutingResolveService implements Resolve<IEmployeeAccount | null> {
  constructor(protected service: EmployeeAccountService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEmployeeAccount | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((employeeAccount: HttpResponse<IEmployeeAccount>) => {
          if (employeeAccount.body) {
            return of(employeeAccount.body);
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
