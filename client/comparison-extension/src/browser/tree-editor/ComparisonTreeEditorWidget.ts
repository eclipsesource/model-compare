import { injectable, inject } from 'inversify';
import { ILogger } from '@theia/core';
//import { ModelServerClient } from '@eclipse-emfcloud/modelserver-theia/lib/common';
//import { ModelServerSubscriptionService } from '@eclipse-emfcloud/modelserver-theia/lib/browser';
import { WorkspaceService } from '@theia/workspace/lib/browser/workspace-service';
import { TreeEditor } from '../tree-widget/interfaces';
//import { NavigatableTreeEditorOptions } from '../tree-widget/navigatable-tree-editor-widget';
import { AddCommandProperty, MasterTreeWidget } from '../tree-widget/master-tree-widget';
import { TreeNode, CompositeTreeNode, Title, Widget } from '@theia/core/lib/browser';
import { ComparisonBackendService } from '../../common/protocol';
import { BaseTreeEditorWidget } from '../tree-widget';


export const ComparisonTreeEditorWidgetOptions = Symbol(
  'ComparisonTreeEditorWidgetOptions'
);
export interface ComparisonTreeEditorWidgetOptions {
  left: string,
  right: string,
  origin: string
}


@injectable()
export class ComparisonTreeEditorWidget extends BaseTreeEditorWidget {

  protected options: ComparisonTreeEditorWidgetOptions;
  protected comparisonResponse: JSONCompareResponse;

  constructor(
    @inject(ComparisonBackendService) readonly comparisonBackendService: ComparisonBackendService,
    @inject(MasterTreeWidget)
    readonly myTreeWidgetOverview: MasterTreeWidget,
    @inject(MasterTreeWidget)
    readonly myTreeWidgetModel1: MasterTreeWidget,
    @inject(MasterTreeWidget)
    readonly myTreeWidgetModel2: MasterTreeWidget,
    @inject(WorkspaceService)
    readonly workspaceService: WorkspaceService,
    @inject(ILogger) readonly logger: ILogger
    //@inject(ModelServerClient)
    //private readonly modelServerApi: ModelServerClient
    //@inject(ModelServerSubscriptionService)
    //private readonly subscriptionService: ModelServerSubscriptionService
  ) {
    super(
      myTreeWidgetOverview,
      myTreeWidgetModel1,
      myTreeWidgetModel2,
      workspaceService,
      logger,
      ComparisonTreeEditorWidget.WIDGET_ID
    );

    //console.log("path: " + this.getModelIDToRequest());
    this.showInformation("loading...", "gray");

    window.onbeforeunload = () => this.dispose();

  }

  public setContent(options) {
    this.options = options;
    this.comparisonBackendService.getNewComparison(options.left, options.right, options.origin).then(r => {
      let response: JSONCompareResponse = JSON.parse(r);
      this.comparisonResponse = response;

      if (response.error.trim() !== "") {
        this.logger.error(response.error);
        this.showInformation(response.error, "red", "fas fa-exclamation-triangle red");
        return;
      }
      this.instanceData = response.overviewTree;

      this.myTreeWidgetOverview
      .setData({ error: false, data: this.instanceData})
      .then(() => this.myTreeWidgetOverview.selectFirst());

      this.myTreeWidgetModel1
      .setData({ error: false, data: response.leftTree})
      .then(() => this.myTreeWidgetModel1.selectFirst());

      this.myTreeWidgetModel2
      .setData({ error: false, data: response.rightTree })
      .then(() => this.myTreeWidgetModel2.selectFirst());

      this.myTreeWidgetOverview.model.refresh();
    });
  }

  /**
   *  Returns the opened file
   
  private getModelIDToRequest(): string {
    const rootUriLength = this.workspaceService
      .getWorkspaceRootUri(this.options.uri)
      .toString().length;
    return this.options.uri.toString().substring(rootUriLength + 1);
  }
  */

  protected treeSelectionChanged(treeWidget: MasterTreeWidget, selectedNodes: readonly Readonly<TreeEditor.Node>[]): void {
    if (treeWidget === this.treeWidgetOverview) {
      if (selectedNodes.length !== 0) {
        this.selectedNode = selectedNodes[0];
        this.navigateToSelection(this.treeWidgetModel1, this.selectedNode.jsonforms.data.uuid);
        this.navigateToSelection(this.treeWidgetModel2, this.selectedNode.jsonforms.data.uuid);
      }
    }
    this.update();
  }

  private navigateToSelection(treeWidget: MasterTreeWidget, uuid: string): void{
    if (!this.selectedNode) {
      return;
    }

    let rootNode = treeWidget.model.root as TreeEditor.RootNode;
    let nodes: Array<TreeNode> = [...rootNode.children];
    treeWidget.model.collapseAll(rootNode).then(() => {
      while (nodes.length !== 0) {
        const node : TreeNode = nodes.pop();
        if (TreeEditor.Node.is(node)) {
          if (node.jsonforms.data.uuid === uuid) {
            let parent: CompositeTreeNode = node.parent;
            while(parent && parent !== rootNode) {
              if (TreeEditor.Node.is(parent)) {
                treeWidget.model.expandNode(parent);
              }
              parent = parent.parent;
            }
            treeWidget.model.selectNode(node);
            return;
          }
          node.children.forEach(element => {
            nodes.push(element);
          });
        }
      }

      // try if there are connectedd uuids
      let relatedUuid = this.comparisonResponse.uuidConnection[uuid];
      if (relatedUuid) {
        this.navigateToSelection(treeWidget, relatedUuid);
      }
    });
  }


  protected deleteNode(node: TreeEditor.Node): void {
    throw new Error("Method not implemented.");
  }
  protected addNode({ node, type, property }: AddCommandProperty): void {
    throw new Error("Method not implemented.");
  }
  protected handleFormUpdate(data: any, node: TreeEditor.Node): void {
    throw new Error("Method not implemented.");
  }

  public save(): void {
    //console.log(this.myTreeWidgetOverview);
  }

  show(): void {
    console.log("show");
    super.show();
    /*
    if (this.delayedRefresh) {
      this.delayedRefresh = false;
      this.treeWidget.model.refresh();
    }
    */
  }

  private showInformation(text: string, color: string, icon: string = "") {
    this.instanceData = {
      eClass: "information",
      name: text,
      icon: icon,
      color: color,
      uuid: "",
      children: []
    }

    this.myTreeWidgetOverview
      .setData({ error: false, data: this.instanceData})
      .then(() => this.myTreeWidgetOverview.selectFirst());
  }

  protected configureTitle(title: Title<Widget>): void {
    title.label = "Comparison Tree Editor";
    title.caption = ComparisonTreeEditorWidget.WIDGET_LABEL;
    title.closable = true;
    //title.iconClass = 'fa coffee-icon dark-purple';
  }
  
}

export namespace ComparisonTreeEditorWidget {
  export const WIDGET_ID = 'json-forms-comparison-tree-editor';
  export const EDITOR_ID = 'com.eclipsesource.comparison.editor';
}

